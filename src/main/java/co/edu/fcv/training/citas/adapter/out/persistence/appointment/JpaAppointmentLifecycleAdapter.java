package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentLifecyclePort;
import co.edu.fcv.training.citas.application.appointment.AppointmentStatusChangedEvent;
import co.edu.fcv.training.citas.domain.appointment.AppointmentLifecycleException;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaAppointmentLifecycleAdapter implements AppointmentLifecyclePort {
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    private final ApplicationEventPublisher events;
    JpaAppointmentLifecycleAdapter(EntityManager entityManager, ApplicationEventPublisher events) { this.entityManager = entityManager; this.events = events; }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummary> list(Long patientUserId, String status, LocalDate from, LocalDate to) {
        var query = entityManager.createNativeQuery(summarySql() + """
                WHERE a.patient_user_id=:patient
                  AND (:status IS NULL OR st.code=:status)
                  AND (:fromDate IS NULL OR DATE(a.scheduled_start_at) >= :fromDate)
                  AND (:toDate IS NULL OR DATE(a.scheduled_start_at) <= :toDate)
                ORDER BY a.scheduled_start_at DESC, a.id DESC
                """);
        query.setParameter("patient", patientUserId).setParameter("status", status)
                .setParameter("fromDate", from).setParameter("toDate", to);
        return ((List<?>) query.getResultList()).stream().map(row -> toSummary((Object[]) row)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentSummary detail(Long patientUserId, Long appointmentId) {
        Object row = entityManager.createNativeQuery(summarySql() + "WHERE a.id=:id AND a.patient_user_id=:patient")
                .setParameter("id", appointmentId).setParameter("patient", patientUserId).getResultList().stream().findFirst().orElse(null);
        if (row == null) throw new AppointmentLifecycleException("APPOINTMENT_NOT_FOUND");
        return toSummary((Object[]) row);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusHistory> history(Long patientUserId, Long appointmentId) {
        detail(patientUserId, appointmentId);
        return entityManager.createNativeQuery("""
                SELECT s.code, h.change_source, h.changed_by_user_id, h.reason, h.changed_at
                FROM appointment_status_history h JOIN appointment_statuses s ON s.id=h.status_id
                WHERE h.appointment_id=:appointment ORDER BY h.changed_at, h.id
                """).setParameter("appointment", appointmentId).getResultList().stream()
                .map(row -> { Object[] x=(Object[]) row; return new StatusHistory((String)x[0], (String)x[1], x[2]==null?null:((Number)x[2]).longValue(), (String)x[3], dateTime(x[4])); }).toList();
    }

    @Override
    @Transactional
    public void cancel(Long patientUserId, Long appointmentId, String reason) {
        Object row = entityManager.createNativeQuery("""
                SELECT a.scheduled_start_at, st.code FROM appointments a JOIN appointment_statuses st ON st.id=a.status_id
                WHERE a.id=:id AND a.patient_user_id=:patient FOR UPDATE
                """).setParameter("id", appointmentId).setParameter("patient", patientUserId).getResultList().stream().findFirst().orElse(null);
        if (row == null) throw new AppointmentLifecycleException("APPOINTMENT_NOT_FOUND");
        Object[] values=(Object[])row;
        if (!dateTime(values[0]).isAfter(LocalDateTime.now(BUSINESS_ZONE))) throw new AppointmentLifecycleException("APPOINTMENT_NOT_FUTURE");
        if (!"APPROVED".equals(values[1]) && !"REQUESTED".equals(values[1])) throw new AppointmentLifecycleException("INVALID_STATUS_TRANSITION");
        Number statusId = ((Number) entityManager.createNativeQuery("SELECT id FROM appointment_statuses WHERE code='CANCELLED'").getSingleResult());
        entityManager.createNativeQuery("UPDATE appointments SET status_id=:status, reason=COALESCE(:reason, reason) WHERE id=:id")
                .setParameter("status", statusId).setParameter("reason", reason).setParameter("id", appointmentId).executeUpdate();
        entityManager.createNativeQuery("UPDATE professional_slots SET appointment_id=NULL WHERE appointment_id=:appointment")
                .setParameter("appointment", appointmentId).executeUpdate();
        entityManager.createNativeQuery("""
                INSERT INTO appointment_status_history (appointment_id,status_id,changed_by_user_id,change_source,reason)
                VALUES (:appointment,:status,:patient,'USER',:reason)
                """).setParameter("appointment", appointmentId).setParameter("status", statusId)
                .setParameter("patient", patientUserId).setParameter("reason", reason).executeUpdate();
        events.publishEvent(new AppointmentStatusChangedEvent(appointmentId, (String) values[1], "CANCELLED", "USER", patientUserId, java.time.Instant.now()));
    }

    private String summarySql() { return """
            SELECT a.id, p.id, CONCAT(pu.first_name,' ',pu.last_name), l.id, l.name,
                   s.id, s.name, a.scheduled_start_at, a.scheduled_end_at, st.code, a.reason
            FROM appointments a
            JOIN professionals p ON p.id=a.professional_id JOIN users pu ON pu.id=p.user_id
            JOIN locations l ON l.id=a.location_id JOIN specialties s ON s.id=a.specialty_id
            JOIN appointment_statuses st ON st.id=a.status_id
            """; }
    private AppointmentSummary toSummary(Object[] x) { return new AppointmentSummary(((Number)x[0]).longValue(),((Number)x[1]).longValue(),(String)x[2],((Number)x[3]).longValue(),(String)x[4],((Number)x[5]).longValue(),(String)x[6],dateTime(x[7]),dateTime(x[8]),(String)x[9],(String)x[10]); }
    private LocalDateTime dateTime(Object value) { if(value instanceof LocalDateTime x)return x; if(value instanceof Timestamp x)return x.toLocalDateTime(); throw new AppointmentLifecycleException("INVALID_APPOINTMENT_DATA"); }
}
