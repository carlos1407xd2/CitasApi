package co.edu.fcv.training.citas.adapter.in.web.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentBookingPort;
import co.edu.fcv.training.citas.application.appointment.BookAppointmentCommand;
import co.edu.fcv.training.citas.application.appointment.BookedAppointment;
import co.edu.fcv.training.citas.application.appointment.AppointmentLifecyclePort;
import co.edu.fcv.training.citas.domain.appointment.AppointmentLifecycleException;
import java.time.LocalDate;
import java.util.List;
import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
class AppointmentController {
    private final AppointmentBookingPort booking;
    private final AppointmentLifecyclePort lifecycle;
    AppointmentController(AppointmentBookingPort booking, AppointmentLifecyclePort lifecycle) { this.booking = booking; this.lifecycle = lifecycle; }

    @org.springframework.web.bind.annotation.GetMapping
    List<AppointmentLifecyclePort.AppointmentSummary> list(Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam(required=false) String status,
            @org.springframework.web.bind.annotation.RequestParam(required=false) LocalDate from,
            @org.springframework.web.bind.annotation.RequestParam(required=false) LocalDate to) {
        return lifecycle.list(userId(authentication), status, from, to);
    }

    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    AppointmentLifecyclePort.AppointmentSummary detail(Authentication authentication, @org.springframework.web.bind.annotation.PathVariable Long id) {
        return lifecycle.detail(userId(authentication), id);
    }

    @org.springframework.web.bind.annotation.GetMapping("/{id}/history")
    List<AppointmentLifecyclePort.StatusHistory> history(Authentication authentication, @org.springframework.web.bind.annotation.PathVariable Long id) {
        return lifecycle.history(userId(authentication), id);
    }

    @org.springframework.web.bind.annotation.PostMapping("/{id}/cancel")
    ResponseEntity<Void> cancel(Authentication authentication, @org.springframework.web.bind.annotation.PathVariable Long id, @RequestBody CancelRequest request) {
        lifecycle.cancel(userId(authentication), id, request == null ? null : request.reason());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<BookingResponse> book(Authentication authentication, @RequestBody BookingRequest request) {
        Long userId = userId(authentication);
        BookedAppointment result = booking.book(new BookAppointmentCommand(
                userId, request.professionalId(), request.locationId(), request.specialtyId(), request.startAt(), request.reason()));
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + result.appointmentId()))
                .body(new BookingResponse(result.appointmentId(), result.status()));
    }

    record BookingRequest(Long professionalId, Long locationId, Long specialtyId,
                          LocalDateTime startAt, String reason) {}
    record BookingResponse(Long appointmentId, String status) {}
    record CancelRequest(String reason) {}
    private Long userId(Authentication authentication) {
        try { return Long.valueOf(authentication.getName()); }
        catch (RuntimeException e) { throw new IllegalStateException("Authenticated subject must be a user id", e); }
    }
}
