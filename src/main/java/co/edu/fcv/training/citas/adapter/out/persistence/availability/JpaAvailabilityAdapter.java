package co.edu.fcv.training.citas.adapter.out.persistence.availability;

import co.edu.fcv.training.citas.application.availability.AvailabilityQuery;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class JpaAvailabilityAdapter implements AvailabilityQuery {
    private final ProfessionalSlotJpaRepository slots;

    JpaAvailabilityAdapter(ProfessionalSlotJpaRepository slots) { this.slots = slots; }

    @Override
    public List<FreeSlot> freeSlots(LocalDate date, Long locationId, Long specialtyId, Long professionalId) {
        return slots.findFreeSlots(date, toShort(locationId), toShort(specialtyId), professionalId).stream()
                .map(x -> new FreeSlot(x.slotId(), x.professionalId(), x.locationId(), x.startAt(), x.endAt()))
                .toList();
    }

    private Short toShort(Long value) {
        if (value == null) return null;
        if (value < 0 || value > Short.MAX_VALUE) throw new IllegalArgumentException("Catalog identifier out of range");
        return value.shortValue();
    }
}
