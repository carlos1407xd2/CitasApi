package co.edu.fcv.training.citas.application.appointment;

public interface AppointmentBookingPort {
    BookedAppointment book(BookAppointmentCommand command);
    void decide(Long adminUserId, Long appointmentId, boolean approve, String reason);
}
