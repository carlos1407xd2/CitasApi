package co.edu.fcv.training.citas.adapter.in.web.catalog;

import co.edu.fcv.training.citas.application.catalog.ServiceCatalogQuery;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogs")
class ServiceCatalogController {
    private final ServiceCatalogQuery catalog;
    ServiceCatalogController(ServiceCatalogQuery catalog) { this.catalog = catalog; }

    @GetMapping("/locations")
    List<LocationResponse> locations() {
        return catalog.activeLocations().stream().map(x -> new LocationResponse(x.id(), x.code(), x.name(), x.address(), x.city(), x.department())).toList();
    }

    @GetMapping("/specialties")
    List<SpecialtyResponse> specialties() {
        return catalog.activeSpecialties().stream().map(x -> new SpecialtyResponse(x.id(), x.code(), x.name(), x.appointmentDurationMinutes(), x.general(), x.requiresAdminApproval())).toList();
    }

    record LocationResponse(Long id, String code, String name, String address, String city, String department) {}
    record SpecialtyResponse(Long id, String code, String name, int appointmentDurationMinutes, boolean general, boolean requiresAdminApproval) {}
}
