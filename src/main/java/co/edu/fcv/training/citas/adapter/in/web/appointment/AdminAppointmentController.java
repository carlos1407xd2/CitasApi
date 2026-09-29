package co.edu.fcv.training.citas.adapter.in.web.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentBookingPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/appointments")
class AdminAppointmentController {
    private final AppointmentBookingPort appointments;
    AdminAppointmentController(AppointmentBookingPort appointments) { this.appointments = appointments; }
    @PostMapping("/{id}/approve") ResponseEntity<Void> approve(Authentication auth, @PathVariable Long id) { appointments.decide(Long.valueOf(auth.getName()), id, true, null); return ResponseEntity.noContent().build(); }
    @PostMapping("/{id}/reject") ResponseEntity<Void> reject(Authentication auth, @PathVariable Long id, @Valid @RequestBody Decision request) { appointments.decide(Long.valueOf(auth.getName()), id, false, request.reason()); return ResponseEntity.noContent().build(); }
    record Decision(String reason) {}
}
