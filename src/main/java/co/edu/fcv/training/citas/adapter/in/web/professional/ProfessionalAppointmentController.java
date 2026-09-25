package co.edu.fcv.training.citas.adapter.in.web.professional;

import co.edu.fcv.training.citas.application.appointment.ProfessionalAppointmentPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/professional/appointments")
class ProfessionalAppointmentController {
    private final ProfessionalAppointmentPort appointments;
    ProfessionalAppointmentController(ProfessionalAppointmentPort appointments) { this.appointments = appointments; }

    @PatchMapping("/{id}/closure")
    ResponseEntity<Void> close(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ClosureRequest request) {
        appointments.close(Long.valueOf(authentication.getName()), id, request.outcome());
        return ResponseEntity.noContent().build();
    }

    record ClosureRequest(@NotBlank String outcome) {}
}
