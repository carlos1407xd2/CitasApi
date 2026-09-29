package co.edu.fcv.training.citas.adapter.in.web.professional;

import co.edu.fcv.training.citas.application.professional.ProfessionalAdministrationPort;
import co.edu.fcv.training.citas.application.professional.ProfessionalAdministrationPort.CreateProfessionalCommand;
import co.edu.fcv.training.citas.application.professional.ProfessionalAdministrationPort.SpecialtyAssignment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.EntityManager;

@RestController
@RequestMapping("/api/v1/admin/professionals")
class AdminProfessionalController {
    private final ProfessionalAdministrationPort professionals;
    private final EntityManager entityManager;
    AdminProfessionalController(ProfessionalAdministrationPort professionals, EntityManager entityManager) { this.professionals = professionals; this.entityManager = entityManager; }

    @org.springframework.web.bind.annotation.GetMapping
    java.util.List<ProfessionalSummary> list() {
        return entityManager.createNativeQuery("SELECT p.id,u.first_name,u.last_name,u.email,p.professional_code,p.license_number,p.active FROM professionals p JOIN users u ON u.id=p.user_id ORDER BY u.last_name,u.first_name").getResultList().stream().map(raw -> { Object[] r=(Object[])raw; return new ProfessionalSummary(((Number)r[0]).longValue(),(String)r[1],(String)r[2],(String)r[3],(String)r[4],(String)r[5],(Boolean)r[6]); }).toList();
    }

    @PostMapping
    ResponseEntity<ProfessionalAdministrationPort.Professional> create(@Valid @RequestBody CreateRequest request) {
        var result = professionals.create(new CreateProfessionalCommand(request.firstName(), request.lastName(), request.documentType(), request.documentNumber(), request.email(), request.phone(), request.password(), request.professionalCode(), request.licenseNumber()));
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}/specialties")
    ResponseEntity<Void> specialties(@PathVariable Long id, @Valid @RequestBody SpecialtyRequest request) {
        professionals.assignSpecialties(id, request.assignments().stream().map(x -> new SpecialtyAssignment(x.specialtyId(), x.primary())).toList());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/locations")
    ResponseEntity<Void> locations(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        professionals.assignLocations(id, request.locationIds());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    ResponseEntity<Void> active(@PathVariable Long id, @Valid @RequestBody ActiveRequest request) {
        professionals.setActive(id, request.active());
        return ResponseEntity.noContent().build();
    }

    record CreateRequest(@NotBlank @Size(max = 80) String firstName, @NotBlank @Size(max = 80) String lastName,
                         @NotBlank @Size(max = 20) String documentType, @NotBlank @Size(max = 40) String documentNumber,
                         @NotBlank @Email @Size(max = 160) String email, @Size(max = 30) String phone,
                         @NotBlank @Size(min = 8, max = 72) String password, @NotBlank @Size(max = 40) String professionalCode,
                         @NotBlank @Size(max = 80) String licenseNumber) {}
    record SpecialtyRequest(@NotEmpty List<SpecialtyAssignmentRequest> assignments) {}
    record SpecialtyAssignmentRequest(Long specialtyId, boolean primary) {}
    record LocationRequest(@NotEmpty List<Long> locationIds) {}
    record ActiveRequest(boolean active) {}
    record ProfessionalSummary(Long id,String firstName,String lastName,String email,String professionalCode,String licenseNumber,Boolean active) {}
}
