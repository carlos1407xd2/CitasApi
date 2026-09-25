package co.edu.fcv.training.citas.application.catalog;

import java.util.List;

public interface ServiceCatalogQuery {
    List<Location> activeLocations();
    List<Specialty> activeSpecialties();

    record Location(Long id, String code, String name, String address, String city, String department) {}
    record Specialty(Long id, String code, String name, int appointmentDurationMinutes, boolean general, boolean requiresAdminApproval) {}
}
