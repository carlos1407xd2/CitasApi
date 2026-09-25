package co.edu.fcv.training.citas.adapter.out.persistence.professional;

import co.edu.fcv.training.citas.application.identity.PasswordHashPort;
import co.edu.fcv.training.citas.application.professional.ProfessionalAdministrationPort;
import co.edu.fcv.training.citas.domain.professional.ProfessionalAdministrationException;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaProfessionalAdministrationAdapter implements ProfessionalAdministrationPort {
    private final EntityManager entityManager;
    private final PasswordHashPort passwordHash;

    JpaProfessionalAdministrationAdapter(EntityManager entityManager, PasswordHashPort passwordHash) {
        this.entityManager = entityManager;
        this.passwordHash = passwordHash;
    }

    @Override
    @Transactional
    public Professional create(CreateProfessionalCommand command) {
        if (scalar("SELECT COUNT(*) FROM users WHERE email = :email", "email", command.email()).intValue() > 0)
            throw new ProfessionalAdministrationException("DUPLICATE_USER");
        if (scalar("SELECT COUNT(*) FROM users WHERE document_type = :type AND document_number = :number", "type", command.documentType(), "number", command.documentNumber()).intValue() > 0)
            throw new ProfessionalAdministrationException("DUPLICATE_USER");
        entityManager.createNativeQuery("""
                INSERT INTO users (first_name,last_name,document_type,document_number,email,phone,password_hash,active)
                VALUES (:firstName,:lastName,:documentType,:documentNumber,:email,:phone,:passwordHash,TRUE)
                """).setParameter("firstName", command.firstName()).setParameter("lastName", command.lastName())
                .setParameter("documentType", command.documentType()).setParameter("documentNumber", command.documentNumber())
                .setParameter("email", command.email()).setParameter("phone", command.phone())
                .setParameter("passwordHash", passwordHash.hash(command.password())).executeUpdate();
        Long userId = scalar("SELECT LAST_INSERT_ID()", null, null).longValue();
        Long roleId = scalar("SELECT id FROM roles WHERE code = 'PROFESSIONAL'", null, null).longValue();
        entityManager.createNativeQuery("INSERT INTO user_roles (user_id, role_id) VALUES (:userId,:roleId)")
                .setParameter("userId", userId).setParameter("roleId", roleId).executeUpdate();
        entityManager.createNativeQuery("""
                INSERT INTO professionals (user_id,professional_code,license_number,active)
                VALUES (:userId,:code,:license,TRUE)
                """).setParameter("userId", userId).setParameter("code", command.professionalCode())
                .setParameter("license", command.licenseNumber()).executeUpdate();
        Long professionalId = scalar("SELECT LAST_INSERT_ID()", null, null).longValue();
        return new Professional(professionalId, userId, command.email(), command.professionalCode(), command.licenseNumber(), true);
    }

    @Override
    @Transactional
    public void assignSpecialties(Long professionalId, List<SpecialtyAssignment> assignments) {
        if (assignments == null || assignments.isEmpty() || assignments.stream().filter(SpecialtyAssignment::primary).count() != 1)
            throw new ProfessionalAdministrationException("PRIMARY_SPECIALTY_REQUIRED");
        ensureProfessional(professionalId);
        entityManager.createNativeQuery("DELETE FROM professional_specialties WHERE professional_id = :id")
                .setParameter("id", professionalId).executeUpdate();
        for (SpecialtyAssignment assignment : assignments) {
            Number exists = scalar("SELECT COUNT(*) FROM specialties WHERE id = :id AND active = TRUE", "id", assignment.specialtyId().shortValue());
            if (exists.intValue() != 1) throw new ProfessionalAdministrationException("INVALID_SPECIALTY");
            entityManager.createNativeQuery("INSERT INTO professional_specialties (professional_id,specialty_id,is_primary,active) VALUES (:professional,:specialty,:primary,TRUE)")
                    .setParameter("professional", professionalId).setParameter("specialty", assignment.specialtyId().shortValue())
                    .setParameter("primary", assignment.primary()).executeUpdate();
        }
    }

    @Override
    @Transactional
    public void assignLocations(Long professionalId, List<Long> locationIds) {
        if (locationIds == null || locationIds.isEmpty()) throw new ProfessionalAdministrationException("LOCATION_REQUIRED");
        ensureProfessional(professionalId);
        entityManager.createNativeQuery("DELETE FROM professional_locations WHERE professional_id = :id").setParameter("id", professionalId).executeUpdate();
        for (Long locationId : locationIds) {
            Number exists = scalar("SELECT COUNT(*) FROM locations WHERE id = :id AND active = TRUE", "id", locationId.shortValue());
            if (exists.intValue() != 1) throw new ProfessionalAdministrationException("INVALID_LOCATION");
            entityManager.createNativeQuery("INSERT INTO professional_locations (professional_id,location_id,active) VALUES (:professional,:location,TRUE)")
                    .setParameter("professional", professionalId).setParameter("location", locationId.shortValue()).executeUpdate();
        }
    }

    @Override
    @Transactional
    public void setActive(Long professionalId, boolean active) {
        ensureProfessional(professionalId);
        entityManager.createNativeQuery("UPDATE professionals SET active = :active WHERE id = :id")
                .setParameter("active", active).setParameter("id", professionalId).executeUpdate();
    }

    private void ensureProfessional(Long id) {
        if (scalar("SELECT COUNT(*) FROM professionals WHERE id = :id", "id", id).intValue() != 1)
            throw new ProfessionalAdministrationException("PROFESSIONAL_NOT_FOUND");
    }
    private Number scalar(String sql, String parameter, Object value) { return scalar(sql, parameter == null ? new Object[0] : new Object[]{parameter, value}); }
    private Number scalar(String sql, Object... args) {
        var query = entityManager.createNativeQuery(sql);
        for (int i = 0; i < args.length; i += 2) query.setParameter((String) args[i], args[i + 1]);
        Object result = query.getResultStream().findFirst().orElse(null);
        if (result == null) throw new ProfessionalAdministrationException("REFERENCE_NOT_FOUND");
        return (Number) result;
    }
}
