package co.edu.fcv.training.citas.application.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentLifecyclePort {
    List<AppointmentSummary> list(Long patientUserId, String status, LocalDate from, LocalDate to);
    AppointmentSummary detail(Long patientUserId, Long appointmentId);
    List<StatusHistory> history(Long patientUserId, Long appointmentId);
    void cancel(Long patientUserId, Long appointmentId, String reason);

    record AppointmentSummary(Long id, Long professionalId, String professionalName, Long locationId,
            String locationName, Long specialtyId, String specialtyName, LocalDateTime startAt,
            LocalDateTime endAt, String status, String reason) {}
    record StatusHistory(String status, String source, Long actorUserId, String reason, LocalDateTime changedAt) {}
}
