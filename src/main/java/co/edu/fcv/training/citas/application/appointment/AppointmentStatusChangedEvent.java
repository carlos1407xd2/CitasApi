package co.edu.fcv.training.citas.application.appointment;

import java.time.Instant;

/** Evento seguro: solo identificadores y estados, nunca PII. */
public record AppointmentStatusChangedEvent(Long appointmentId, String previousStatus, String newStatus,
                                            String source, Long actorUserId, Instant occurredAt) {}
