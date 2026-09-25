package co.edu.fcv.training.citas.application.appointment;

public interface AppointmentBookingPort {
    BookedAppointment book(BookAppointmentCommand command);
}
