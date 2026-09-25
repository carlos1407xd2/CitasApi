package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import co.edu.fcv.training.citas.application.catalog.ServiceCatalogQuery;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class JpaServiceCatalogAdapter implements ServiceCatalogQuery {
    private final LocationJpaRepository locations;
    private final SpecialtyJpaRepository specialties;

    JpaServiceCatalogAdapter(LocationJpaRepository locations, SpecialtyJpaRepository specialties) {
        this.locations = locations;
        this.specialties = specialties;
    }

    @Override public List<Location> activeLocations() {
        return locations.findAllByActiveTrueOrderByNameAsc().stream()
                .map(x -> new Location(x.id().longValue(), x.code(), x.name(), x.address(), x.city(), x.department())).toList();
    }

    @Override public List<Specialty> activeSpecialties() {
        return specialties.findAllByActiveTrueOrderByNameAsc().stream()
                .map(x -> new Specialty(x.id().longValue(), x.code(), x.name(), x.duration(), x.general(), x.requiresAdminApproval())).toList();
    }
}
