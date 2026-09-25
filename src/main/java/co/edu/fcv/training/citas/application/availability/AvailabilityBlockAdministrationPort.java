package co.edu.fcv.training.citas.application.availability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityBlockAdministrationPort {
    List<Block> list(Long userId);
    Block create(Long userId, CreateBlock command);
    Block update(Long userId, Long blockId, UpdateBlock command);
    void delete(Long userId, Long blockId);

    record CreateBlock(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime) {}
    record UpdateBlock(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime, Boolean active) {}
    record Block(Long id, Long professionalId, Long locationId, LocalDate date,
                 LocalTime startTime, LocalTime endTime, boolean active) {}
}
