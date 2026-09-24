package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.fcv.training.citas.application.identity.UserRegistrationPort.PersistedRegistration;
import co.edu.fcv.training.citas.domain.identity.InvalidInsurancePlanException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaUserRegistrationAdapterTest {

    @Mock private UserJpaRepository users;
    @Mock private RoleJpaRepository roles;
    @Mock private EpsPlanJpaRepository plans;
    @Mock private UserInsuranceAffiliationJpaRepository affiliations;

    @Test
    void rejectsAnInactivePlanBeforeCreatingTheUser() {
        when(users.existsByEmailIgnoreCase("user@example.test")).thenReturn(false);
        when(users.existsByDocumentTypeAndDocumentNumber("CC", "900001")).thenReturn(false);
        when(plans.findById(5L)).thenReturn(Optional.of(new EpsPlanEntity()));

        JpaUserRegistrationAdapter adapter = new JpaUserRegistrationAdapter(users, roles, plans, affiliations);

        assertThatThrownBy(() -> adapter.register(registration()))
                .isInstanceOf(InvalidInsurancePlanException.class);

        verify(users, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
        verify(affiliations, never()).save(org.mockito.ArgumentMatchers.any());
    }

    private PersistedRegistration registration() {
        return new PersistedRegistration("User", "Test", "CC", "900001", "user@example.test", "3000000000",
                "bcrypt-hash", 5L, "AF-LAB-test");
    }
}
