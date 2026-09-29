package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.ProfessionalAppointmentPort;
import co.edu.fcv.training.citas.application.appointment.AppointmentStatusChangedEvent;
import co.edu.fcv.training.citas.domain.appointment.AppointmentLifecycleException;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaProfessionalAppointmentAdapter implements ProfessionalAppointmentPort {
    private static final ZoneId ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    private final ApplicationEventPublisher events;
    JpaProfessionalAppointmentAdapter(EntityManager entityManager, ApplicationEventPublisher events) { this.entityManager = entityManager; this.events = events; }

    @Override
    public List<ProfessionalAppointmentPort.AgendaAppointment> agenda(Long professionalUserId, LocalDate from, LocalDate to) {
        return entityManager.createNativeQuery("""
                SELECT a.id,a.patient_user_id,CONCAT(u.first_name,' ',u.last_name),sp.name,l.name,
                       a.scheduled_start_at,a.scheduled_end_at,st.code
                FROM appointments a JOIN professionals p ON p.id=a.professional_id
                JOIN users u ON u.id=a.patient_user_id JOIN specialties sp ON sp.id=a.specialty_id
                JOIN locations l ON l.id=a.location_id JOIN appointment_statuses st ON st.id=a.status_id
                WHERE p.user_id=:user AND st.code='APPROVED' AND a.scheduled_start_at>=:from AND a.scheduled_start_at<:to
                ORDER BY a.scheduled_start_at
                """).setParameter("user", professionalUserId).setParameter("from", from.atStartOfDay()).setParameter("to", to.plusDays(1).atStartOfDay()).getResultList().stream().map(raw -> { Object[] r=(Object[])raw; return new ProfessionalAppointmentPort.AgendaAppointment(((Number)r[0]).longValue(),((Number)r[1]).longValue(),(String)r[2],(String)r[3],(String)r[4],r[5].toString(),r[6].toString(),(String)r[7]); }).toList();
    }

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
        events.publishEvent(new AppointmentStatusChangedEvent(appointmentId, (String) row[1], outcome, "USER", professionalUserId, java.time.Instant.now()));
    }

    private LocalDateTime dateTime(Object value) {
        if (value instanceof LocalDateTime x) return x;
        if (value instanceof java.sql.Timestamp x) return x.toLocalDateTime();
        throw new AppointmentLifecycleException("INVALID_APPOINTMENT_DATA");
    }
}
