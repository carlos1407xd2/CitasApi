package co.edu.fcv.training.citas.adapter.in.web.appointment;

import co.edu.fcv.training.citas.application.appointment.ReschedulePort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/reschedule-requests")
class AdminRescheduleController {
    private final ReschedulePort reschedule;
    AdminRescheduleController(ReschedulePort reschedule) { this.reschedule = reschedule; }
    @PostMapping("/{id}/approve") ResponseEntity<Void> approve(Authentication auth, @PathVariable Long id, @RequestBody(required=false) Decision request) { reschedule.decideReschedule(Long.valueOf(auth.getName()), id, true, request == null ? null : request.reason()); return ResponseEntity.noContent().build(); }
    @PostMapping("/{id}/reject") ResponseEntity<Void> reject(Authentication auth, @PathVariable Long id, @RequestBody(required=false) Decision request) { reschedule.decideReschedule(Long.valueOf(auth.getName()), id, false, request == null ? null : request.reason()); return ResponseEntity.noContent().build(); }
    record Decision(String reason) {}
}
