package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentBookingPort;
import co.edu.fcv.training.citas.application.appointment.BookAppointmentCommand;
import co.edu.fcv.training.citas.application.appointment.BookedAppointment;
import co.edu.fcv.training.citas.application.appointment.AppointmentStatusChangedEvent;
import co.edu.fcv.training.citas.domain.appointment.AppointmentBookingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaAppointmentBookingAdapter implements AppointmentBookingPort {
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    private final ApplicationEventPublisher events;

    JpaAppointmentBookingAdapter(EntityManager entityManager, ApplicationEventPublisher events) { this.entityManager = entityManager; this.events = events; }

    @Override
    @Transactional
    public BookedAppointment book(BookAppointmentCommand command) {
        validateCommand(command);
        Tuple specialty = one("""
                SELECT appointment_duration_minutes AS duration, is_general AS general,
                       requires_admin_approval AS approval
                FROM specialties WHERE id = :id AND active = TRUE
                """, "id", command.specialtyId());
        if (specialty == null) throw new AppointmentBookingException("INACTIVE_SPECIALTY");
        int duration = ((Number) specialty.get("duration")).intValue();
        boolean general = ((Boolean) specialty.get("general"));
        boolean requiresApproval = ((Boolean) specialty.get("approval"));
        if (!offerIsValid(command, specialty)) throw new AppointmentBookingException("INVALID_OFFER");

        LocalDateTime endAt = command.startAt().plusMinutes(duration);
        List<Tuple> slots = entityManager.createNativeQuery("""
                SELECT ps.id AS id, ps.start_at AS startAt, ps.end_at AS endAt
                FROM professional_slots ps
                JOIN availability_blocks ab ON ab.id = ps.availability_block_id
                JOIN professionals p ON p.id = ab.professional_id
                JOIN professional_locations pl ON pl.professional_id = ab.professional_id
                    AND pl.location_id = ab.location_id AND pl.active = TRUE
                JOIN professional_specialties psp ON psp.professional_id = ab.professional_id
                    AND psp.specialty_id = :specialtyId AND psp.active = TRUE
                WHERE ps.appointment_id IS NULL AND ab.active = TRUE
                  AND p.active = TRUE AND ab.location_id = :locationId
                  AND ab.professional_id = :professionalId
                  AND ps.start_at >= :startAt AND ps.end_at <= :endAt
                ORDER BY ps.start_at
                FOR UPDATE
                """, Tuple.class)
                .setParameter("specialtyId", command.specialtyId().shortValue())
                .setParameter("locationId", command.locationId().shortValue())
                .setParameter("professionalId", command.professionalId())
                .setParameter("startAt", command.startAt())
                .setParameter("endAt", endAt)
                .getResultList();
        if (slots.size() != duration / 30 || !areConsecutive(slots)) {
            throw new AppointmentBookingException("SLOT_NOT_AVAILABLE");
        }

        String status = general && !requiresApproval ? "APPROVED" : "REQUESTED";
        Number statusId = scalar("SELECT id FROM appointment_statuses WHERE code = :code", "code", status);
        Number affiliationId = scalar("SELECT id FROM user_insurance_affiliations WHERE user_id=:patient AND is_current=TRUE ORDER BY valid_from DESC LIMIT 1", "patient", command.patientUserId());
        entityManager.createNativeQuery("""
                INSERT INTO appointments (patient_user_id, professional_id, location_id, specialty_id,
                  insurance_affiliation_id, status_id, reason, scheduled_start_at, scheduled_end_at, created_by_user_id)
                VALUES (:patient, :professional, :location, :specialty, :insurance, :status, :reason, :startAt, :endAt, :patient)
                """)
                .setParameter("patient", command.patientUserId())
                .setParameter("professional", command.professionalId())
                .setParameter("location", command.locationId().shortValue())
                .setParameter("specialty", command.specialtyId().shortValue())
                .setParameter("insurance", affiliationId)
                .setParameter("status", statusId)
                .setParameter("reason", command.reason())
                .setParameter("startAt", command.startAt())
                .setParameter("endAt", endAt)
                .executeUpdate();
        Number id = scalar("SELECT LAST_INSERT_ID()", null, null);
        entityManager.createNativeQuery("UPDATE professional_slots SET appointment_id = :appointment WHERE id IN (:ids)")
                .setParameter("appointment", id)
                .setParameter("ids", slots.stream().map(x -> ((Number) x.get("id")).longValue()).toList())
                .executeUpdate();
        entityManager.createNativeQuery("""
                INSERT INTO appointment_status_history (appointment_id, status_id, changed_by_user_id, change_source, reason)
                VALUES (:appointment, :status, :patient, 'USER', :reason)
                """).setParameter("appointment", id).setParameter("status", statusId)
                .setParameter("patient", command.patientUserId()).setParameter("reason", command.reason()).executeUpdate();
        events.publishEvent(new AppointmentStatusChangedEvent(id.longValue(), null, status, "USER", command.patientUserId(), java.time.Instant.now()));
        return new BookedAppointment(id.longValue(), status);
    }

    @Override
    @Transactional
    public void decide(Long adminUserId, Long appointmentId, boolean approve, String reason) {
        if (!approve && (reason == null || reason.isBlank())) throw new AppointmentBookingException("REJECTION_REASON_REQUIRED");
        Tuple appointment = one("SELECT a.status_id AS statusId, a.patient_user_id AS patientId, s.code AS previousStatus, a.scheduled_start_at AS startAt, a.scheduled_end_at AS endAt FROM appointments a JOIN appointment_statuses s ON s.id=a.status_id WHERE a.id=:id AND s.code='REQUESTED' FOR UPDATE", "id", appointmentId);
        if (appointment == null) throw new AppointmentBookingException("APPOINTMENT_NOT_PENDING");
        String next = approve ? "APPROVED" : "REJECTED";
        Number nextId = scalar("SELECT id FROM appointment_statuses WHERE code=:code", "code", next);
        entityManager.createNativeQuery("UPDATE appointments SET status_id=:status, approved_by_user_id=:admin, approved_at=CASE WHEN :approve = TRUE THEN NOW() ELSE NULL END WHERE id=:id")
                .setParameter("status", nextId).setParameter("admin", adminUserId).setParameter("approve", approve).setParameter("id", appointmentId).executeUpdate();
        if (!approve) entityManager.createNativeQuery("UPDATE professional_slots ps JOIN appointments a ON a.id=:id SET ps.appointment_id=NULL WHERE ps.appointment_id=:id").setParameter("id", appointmentId).executeUpdate();
        entityManager.createNativeQuery("INSERT INTO appointment_status_history (appointment_id,status_id,changed_by_user_id,change_source,reason) VALUES (:appointment,:status,:admin,'ADMIN',:reason)")
                .setParameter("appointment", appointmentId).setParameter("status", nextId).setParameter("admin", adminUserId).setParameter("reason", reason).executeUpdate();
        events.publishEvent(new AppointmentStatusChangedEvent(appointmentId, (String) appointment.get("previousStatus"), next, "ADMIN", adminUserId, java.time.Instant.now()));
    }

    private boolean offerIsValid(BookAppointmentCommand command, Tuple ignored) {
        Number count = scalar("""
                SELECT COUNT(*) FROM professionals p
                JOIN professional_locations pl ON pl.professional_id = p.id AND pl.location_id = :location AND pl.active = TRUE
                JOIN professional_specialties ps ON ps.professional_id = p.id AND ps.specialty_id = :specialty AND ps.active = TRUE
                JOIN locations l ON l.id = :location AND l.active = TRUE
                WHERE p.id = :professional AND p.active = TRUE
                """, MapArgs.of("location", command.locationId().shortValue(), "specialty", command.specialtyId().shortValue(), "professional", command.professionalId()));
        return count != null && count.intValue() == 1;
    }

    private boolean areConsecutive(List<Tuple> slots) {
        for (int i = 1; i < slots.size(); i++) {
            if (!dateTime(slots.get(i - 1).get("endAt"))
                    .equals(dateTime(slots.get(i).get("startAt")))) return false;
        }
        return true;
    }

    private LocalDateTime dateTime(Object value) {
        if (value instanceof LocalDateTime localDateTime) return localDateTime;
        if (value instanceof java.sql.Timestamp timestamp) return timestamp.toLocalDateTime();
        throw new AppointmentBookingException("INVALID_SLOT_DATA");
    }

    private void validateCommand(BookAppointmentCommand command) {
        if (command.patientUserId() == null || command.professionalId() == null || command.locationId() == null
                || command.specialtyId() == null || command.startAt() == null) throw new AppointmentBookingException("VALIDATION_ERROR");
        if (command.startAt().getSecond() != 0 || command.startAt().getNano() != 0 || command.startAt().getMinute() % 30 != 0
                || !command.startAt().isAfter(LocalDateTime.now(BUSINESS_ZONE))) throw new AppointmentBookingException("INVALID_START_TIME");
    }

    private Tuple one(String sql, String parameter, Object value) {
        var query = entityManager.createNativeQuery(sql, Tuple.class);
        if (parameter != null) query.setParameter(parameter, value);
        List<?> rows = query.getResultList();
        return rows.isEmpty() ? null : (Tuple) rows.get(0);
    }
    private Number scalar(String sql, String parameter, Object value) { return (Number) oneScalar(sql, parameter, value); }
    private Object oneScalar(String sql, String parameter, Object value) {
        var query = entityManager.createNativeQuery(sql);
        if (parameter != null) query.setParameter(parameter, value);
        return query.getResultStream().findFirst().orElse(null);
    }
    private Number scalar(String sql, MapArgs args) {
        var query = entityManager.createNativeQuery(sql);
        args.values.forEach(query::setParameter);
        return (Number) query.getSingleResult();
    }
    private record MapArgs(java.util.Map<String, Object> values) {
        static MapArgs of(Object... values) {
            var map = new java.util.HashMap<String, Object>();
            for (int i = 0; i < values.length; i += 2) map.put((String) values[i], values[i + 1]);
            return new MapArgs(map);
        }
    }
}
