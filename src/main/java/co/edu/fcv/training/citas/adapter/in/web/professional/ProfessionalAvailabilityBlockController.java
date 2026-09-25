package co.edu.fcv.training.citas.adapter.in.web.professional;

import co.edu.fcv.training.citas.application.availability.AvailabilityBlockAdministrationPort;
import co.edu.fcv.training.citas.application.availability.AvailabilityBlockAdministrationPort.CreateBlock;
import co.edu.fcv.training.citas.application.availability.AvailabilityBlockAdministrationPort.UpdateBlock;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/professional/availability-blocks")
class ProfessionalAvailabilityBlockController {
    private final AvailabilityBlockAdministrationPort blocks;
    ProfessionalAvailabilityBlockController(AvailabilityBlockAdministrationPort blocks) { this.blocks = blocks; }

    @GetMapping
    ListResponse list(Authentication authentication) { return new ListResponse(blocks.list(userId(authentication))); }

    @PostMapping
    ResponseEntity<AvailabilityBlockAdministrationPort.Block> create(Authentication authentication, @Valid @RequestBody CreateRequest request) {
        var block = blocks.create(userId(authentication), new CreateBlock(request.locationId(), request.date(), request.startTime(), request.endTime()));
        return ResponseEntity.status(201).body(block);
    }

    @PatchMapping("/{id}")
    AvailabilityBlockAdministrationPort.Block update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody UpdateRequest request) {
        return blocks.update(userId(authentication), id, new UpdateBlock(request.locationId(), request.date(), request.startTime(), request.endTime(), request.active()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        blocks.delete(userId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    private Long userId(Authentication authentication) { return Long.valueOf(authentication.getName()); }
    record ListResponse(java.util.List<AvailabilityBlockAdministrationPort.Block> blocks) {}
    record CreateRequest(@NotNull Long locationId, @NotNull LocalDate date, @NotNull LocalTime startTime, @NotNull LocalTime endTime) {}
    record UpdateRequest(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime, Boolean active) {}
}
