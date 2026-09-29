package co.edu.fcv.training.citas.adapter.in.web.appointment;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/inbox")
class AdminInboxController {
    private final EntityManager entityManager;
    AdminInboxController(EntityManager entityManager) { this.entityManager = entityManager; }

    @GetMapping
    InboxResponse list(Authentication ignored, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to,
                       @RequestParam(required = false) Long locationId, @RequestParam(required = false) Long professionalId,
                       @RequestParam(required = false) Long specialtyId) {
        LocalDate start = from == null ? LocalDate.now() : from;
        LocalDate end = to == null ? start.plusMonths(1) : to;
        List<PendingItem> items = new ArrayList<>();
        String filters = (locationId == null ? "" : " AND a.location_id=:locationId")
                + (professionalId == null ? "" : " AND a.professional_id=:professionalId")
                + (specialtyId == null ? "" : " AND a.specialty_id=:specialtyId");
        var appointmentsQuery = entityManager.createNativeQuery("""
                SELECT a.id, 'APPOINTMENT', s.code, sp.name, l.name, a.scheduled_start_at
                FROM appointments a JOIN appointment_statuses s ON s.id=a.status_id
                JOIN specialties sp ON sp.id=a.specialty_id JOIN locations l ON l.id=a.location_id
                WHERE s.code='REQUESTED' AND a.scheduled_start_at >= :from AND a.scheduled_start_at < :to
                """ + filters + """
                ORDER BY a.scheduled_start_at
                """).setParameter("from", start.atStartOfDay()).setParameter("to", end.plusDays(1).atStartOfDay());
        if (locationId != null) appointmentsQuery.setParameter("locationId", locationId);
        if (professionalId != null) appointmentsQuery.setParameter("professionalId", professionalId);
        if (specialtyId != null) appointmentsQuery.setParameter("specialtyId", specialtyId);
        for (Object row : appointmentsQuery.getResultList()) {
            Object[] r = (Object[]) row; items.add(new PendingItem(((Number) r[0]).longValue(), (String) r[1], (String) r[2], (String) r[3], (String) r[4], r[5].toString()));
        }
        var reschedulesQuery = entityManager.createNativeQuery("""
                SELECT r.id, 'RESCHEDULE', rs.code, sp.name, l.name, r.requested_start_at
                FROM reschedule_requests r JOIN reschedule_request_statuses rs ON rs.id=r.status_id
                JOIN appointments a ON a.id=r.appointment_id JOIN specialties sp ON sp.id=a.specialty_id
                JOIN locations l ON l.id=r.requested_location_id
                WHERE rs.code='PENDING' AND r.requested_start_at >= :from AND r.requested_start_at < :to
                """ + filters.replace("a.", "a.") + """
                ORDER BY r.requested_start_at
                """).setParameter("from", start.atStartOfDay()).setParameter("to", end.plusDays(1).atStartOfDay());
        if (locationId != null) reschedulesQuery.setParameter("locationId", locationId);
        if (professionalId != null) reschedulesQuery.setParameter("professionalId", professionalId);
        if (specialtyId != null) reschedulesQuery.setParameter("specialtyId", specialtyId);
        for (Object row : reschedulesQuery.getResultList()) {
            Object[] r = (Object[]) row; items.add(new PendingItem(((Number) r[0]).longValue(), (String) r[1], (String) r[2], (String) r[3], (String) r[4], r[5].toString()));
        }
        return new InboxResponse(items);
    }
    record InboxResponse(List<PendingItem> items) {}
    record PendingItem(Long id, String type, String status, String specialty, String location, String startAt) {}
}
