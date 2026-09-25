package co.edu.fcv.training.citas.application.appointment;

import java.time.LocalDateTime;

public record BookAppointmentCommand(Long patientUserId, Long professionalId, Long locationId,
                                     Long specialtyId, LocalDateTime startAt, String reason) {}
