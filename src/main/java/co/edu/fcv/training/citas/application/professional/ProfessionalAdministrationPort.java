package co.edu.fcv.training.citas.application.professional;

import java.util.List;

public interface ProfessionalAdministrationPort {
    Professional create(CreateProfessionalCommand command);
    void assignSpecialties(Long professionalId, List<SpecialtyAssignment> assignments);
    void assignLocations(Long professionalId, List<Long> locationIds);
    void setActive(Long professionalId, boolean active);

    record CreateProfessionalCommand(String firstName, String lastName, String documentType,
                                     String documentNumber, String email, String phone,
                                     String password, String professionalCode, String licenseNumber) {}
    record SpecialtyAssignment(Long specialtyId, boolean primary) {}
    record Professional(Long id, Long userId, String email, String professionalCode, String licenseNumber, boolean active) {}
}
