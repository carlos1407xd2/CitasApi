package co.edu.fcv.training.citas.application.appointment;

public interface ProfessionalAppointmentPort {
    void close(Long professionalUserId, Long appointmentId, String outcome);
}
