package co.edu.fcv.training.citas.adapter.in.web.appointment;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/audit")
class AdminAuditController {
    private final EntityManager entityManager;
    AdminAuditController(EntityManager entityManager) { this.entityManager = entityManager; }
    @GetMapping("/appointments/{appointmentId}/history")
    List<AuditEntry> history(Authentication ignored, @PathVariable Long appointmentId) {
        return entityManager.createNativeQuery("SELECT h.status_id,s.code,h.change_source,h.changed_by_user_id,h.reason,h.changed_at FROM appointment_status_history h JOIN appointment_statuses s ON s.id=h.status_id WHERE h.appointment_id=:id ORDER BY h.changed_at").setParameter("id",appointmentId).getResultList().stream().map(raw->{Object[] r=(Object[])raw; return new AuditEntry(((Number)r[0]).longValue(),(String)r[1],(String)r[2],r[3]==null?null:((Number)r[3]).longValue(),(String)r[4],r[5].toString());}).toList();
    }
    record AuditEntry(Long statusId,String status,String source,Long actorUserId,String reason,String changedAt) {}
}
