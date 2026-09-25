package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.ProfessionalAppointmentPort;
import co.edu.fcv.training.citas.domain.appointment.AppointmentLifecycleException;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaProfessionalAppointmentAdapter implements ProfessionalAppointmentPort {
    private static final ZoneId ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    JpaProfessionalAppointmentAdapter(EntityManager entityManager) { this.entityManager = entityManager; }

    @Override @Transactional
    public void close(Long professionalUserId, Long appointmentId, String outcome) {
        if (!"COMPLETED".equals(outcome) && !"NO_SHOW".equals(outcome)) throw new AppointmentLifecycleException("INVALID_CLOSURE_STATUS");
        Object raw = entityManager.createNativeQuery("""
                SELECT a.scheduled_end_at, st.code FROM appointments a
                JOIN professionals p ON p.id=a.professional_id
                JOIN appointment_statuses st ON st.id=a.status_id
                WHERE a.id=:appointment AND p.user_id=:user AND p.active=TRUE FOR UPDATE
                """).setParameter("appointment", appointmentId).setParameter("user", professionalUserId)
                .getResultList().stream().findFirst().orElse(null);
        if (raw == null) throw new AppointmentLifecycleException("APPOINTMENT_NOT_FOUND");
        Object[] row = (Object[]) raw;
        if (!"APPROVED".equals(row[1])) throw new AppointmentLifecycleException("INVALID_STATUS_TRANSITION");
        if (!dateTime(row[0]).isBefore(LocalDateTime.now(ZONE))) throw new AppointmentLifecycleException("APPOINTMENT_NOT_FINISHED");
        Number status = (Number) entityManager.createNativeQuery("SELECT id FROM appointment_statuses WHERE code=:code")
                .setParameter("code", outcome).getSingleResult();
        entityManager.createNativeQuery("UPDATE appointments SET status_id=:status WHERE id=:appointment")
                .setParameter("status", status).setParameter("appointment", appointmentId).executeUpdate();
        entityManager.createNativeQuery("""
                INSERT INTO appointment_status_history (appointment_id,status_id,changed_by_user_id,change_source,reason)
                VALUES (:appointment,:status,:user,'USER',NULL)
                """).setParameter("appointment", appointmentId).setParameter("status", status)
                .setParameter("user", professionalUserId).executeUpdate();
    }

    private LocalDateTime dateTime(Object value) {
        if (value instanceof LocalDateTime x) return x;
        if (value instanceof java.sql.Timestamp x) return x.toLocalDateTime();
        throw new AppointmentLifecycleException("INVALID_APPOINTMENT_DATA");
    }
}
