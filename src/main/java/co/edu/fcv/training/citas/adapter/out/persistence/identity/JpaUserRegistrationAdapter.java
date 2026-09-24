package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import co.edu.fcv.training.citas.application.identity.RegisteredUser;
import co.edu.fcv.training.citas.application.identity.UserRegistrationPort;
import co.edu.fcv.training.citas.domain.identity.DuplicateUserException;
import co.edu.fcv.training.citas.domain.identity.InvalidInsurancePlanException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaUserRegistrationAdapter implements UserRegistrationPort {

    private final UserJpaRepository users;
    private final RoleJpaRepository roles;
    private final EpsPlanJpaRepository plans;
    private final UserInsuranceAffiliationJpaRepository affiliations;

    JpaUserRegistrationAdapter(UserJpaRepository users, RoleJpaRepository roles, EpsPlanJpaRepository plans,
            UserInsuranceAffiliationJpaRepository affiliations) {
        this.users = users;
        this.roles = roles;
        this.plans = plans;
        this.affiliations = affiliations;
    }

    @Override
    @Transactional
    public RegisteredUser register(PersistedRegistration registration) {
        if (users.existsByEmailIgnoreCase(registration.email())) {
            throw new DuplicateUserException("email already registered");
        }
        if (users.existsByDocumentTypeAndDocumentNumber(registration.documentType(), registration.documentNumber())) {
            throw new DuplicateUserException("document already registered");
        }

        EpsPlanEntity plan = null;
        if (registration.insurancePlanId() != null) {
            plan = plans.findById(registration.insurancePlanId())
                    .filter(EpsPlanEntity::isActive)
                    .orElseThrow(InvalidInsurancePlanException::new);
        }

        RoleEntity userRole = roles.findByCode("USER")
                .orElseThrow(() -> new IllegalStateException("USER role seed is missing"));
        UserEntity user = users.saveAndFlush(UserEntity.register(
                registration.firstName(), registration.lastName(), registration.documentType(),
                registration.documentNumber(), registration.email(), registration.phone(),
                registration.passwordHash(), userRole));

        if (plan != null) {
            affiliations.save(UserInsuranceAffiliationEntity.current(user, plan, registration.membershipNumber()));
        }
        return new RegisteredUser(user.id(), user.email(), registration.insurancePlanId());
    }
}
