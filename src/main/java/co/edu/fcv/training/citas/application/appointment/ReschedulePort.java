package co.edu.fcv.training.citas.application.appointment;

import java.time.LocalDateTime;

public interface ReschedulePort {
    Long requestReschedule(Long patientUserId, Long appointmentId, Long locationId, LocalDateTime startAt);
    void decideReschedule(Long adminUserId, Long requestId, boolean approve, String reason);
}
