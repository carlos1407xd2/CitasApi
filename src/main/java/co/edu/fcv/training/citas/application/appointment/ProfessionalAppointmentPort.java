package co.edu.fcv.training.citas.application.appointment;

import java.time.LocalDate;
import java.util.List;

public interface ProfessionalAppointmentPort {
    List<AgendaAppointment> agenda(Long professionalUserId, LocalDate from, LocalDate to);
    void close(Long professionalUserId, Long appointmentId, String outcome);
    record AgendaAppointment(Long id, Long patientId, String patientName, String specialty, String location, String startAt, String endAt, String status) {}
}
