package co.edu.fcv.training.citas.adapter.in.web.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentBookingPort;
import co.edu.fcv.training.citas.application.appointment.BookAppointmentCommand;
import co.edu.fcv.training.citas.application.appointment.BookedAppointment;
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
    AppointmentController(AppointmentBookingPort booking) { this.booking = booking; }

    @PostMapping
    ResponseEntity<BookingResponse> book(Authentication authentication, @RequestBody BookingRequest request) {
        Long userId;
        try { userId = Long.valueOf(authentication.getName()); }
        catch (RuntimeException e) { throw new IllegalStateException("Authenticated subject must be a user id", e); }
        BookedAppointment result = booking.book(new BookAppointmentCommand(
                userId, request.professionalId(), request.locationId(), request.specialtyId(), request.startAt(), request.reason()));
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + result.appointmentId()))
                .body(new BookingResponse(result.appointmentId(), result.status()));
    }

    record BookingRequest(Long professionalId, Long locationId, Long specialtyId,
                          LocalDateTime startAt, String reason) {}
    record BookingResponse(Long appointmentId, String status) {}
}
