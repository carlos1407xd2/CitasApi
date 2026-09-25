package co.edu.fcv.training.citas.adapter.out.persistence.availability;

import java.time.LocalDateTime;

record FreeSlotProjection(Long slotId, Long professionalId, Long locationId,
                          LocalDateTime startAt, LocalDateTime endAt) {}
