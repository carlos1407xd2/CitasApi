package co.edu.fcv.training.citas.application.availability;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityQuery {
    List<FreeSlot> freeSlots(LocalDate date, Long locationId, Long specialtyId, Long professionalId);

    record FreeSlot(Long slotId, Long professionalId, Long locationId, LocalDateTime startAt, LocalDateTime endAt) {}
}
