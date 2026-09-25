package co.edu.fcv.training.citas.adapter.in.web.availability;

import co.edu.fcv.training.citas.application.availability.AvailabilityQuery;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/availability")
class AvailabilityController {
    private final AvailabilityQuery availability;
    AvailabilityController(AvailabilityQuery availability) { this.availability = availability; }

    @GetMapping
    List<FreeSlotResponse> freeSlots(
            @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false) Long professionalId) {
        return availability.freeSlots(date, locationId, specialtyId, professionalId).stream()
                .map(x -> new FreeSlotResponse(x.slotId(), x.professionalId(), x.locationId(), x.startAt(), x.endAt()))
                .toList();
    }

    record FreeSlotResponse(Long slotId, Long professionalId, Long locationId,
                            java.time.LocalDateTime startAt, java.time.LocalDateTime endAt) {}
}
