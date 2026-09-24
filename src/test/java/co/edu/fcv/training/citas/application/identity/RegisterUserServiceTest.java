package co.edu.fcv.training.citas.application.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock private UserRegistrationPort registrations;
    @Mock private PasswordHashPort passwordHash;
    @Mock private MembershipNumberPort membershipNumbers;
    @Captor private ArgumentCaptor<UserRegistrationPort.PersistedRegistration> persisted;

    @Test
    void registersWithoutAnAffiliationWhenPlanIsOmitted() {
        RegisterUserService service = service();
        when(passwordHash.hash("correct horse battery staple")).thenReturn("bcrypt-hash");
        when(registrations.register(any())).thenAnswer(invocation -> {
            var value = invocation.getArgument(0, UserRegistrationPort.PersistedRegistration.class);
            return new RegisteredUser(7L, value.email(), value.insurancePlanId());
        });

        RegisteredUser result = service.register(command(null));

        verify(registrations).register(persisted.capture());
        assertThat(result.insurancePlanId()).isNull();
        assertThat(persisted.getValue().passwordHash()).isEqualTo("bcrypt-hash");
        assertThat(persisted.getValue().membershipNumber()).isNull();
        verify(membershipNumbers, never()).next();
    }

    @Test
    void createsAnAffiliationReferenceForAnActivePlan() {
        RegisterUserService service = service();
        when(passwordHash.hash("correct horse battery staple")).thenReturn("bcrypt-hash");
        when(membershipNumbers.next()).thenReturn("AF-LAB-test-id");
        when(registrations.register(any())).thenAnswer(invocation -> {
            var value = invocation.getArgument(0, UserRegistrationPort.PersistedRegistration.class);
            return new RegisteredUser(8L, value.email(), value.insurancePlanId());
        });

        RegisteredUser result = service.register(command(4L));

        verify(registrations).register(persisted.capture());
        assertThat(result.insurancePlanId()).isEqualTo(4L);
        assertThat(persisted.getValue().membershipNumber()).isEqualTo("AF-LAB-test-id");
        assertThat(persisted.getValue().email()).isEqualTo("user@example.test");
        assertThat(persisted.getValue().documentType()).isEqualTo("CC");
    }

    private RegisterUserService service() {
        return new RegisterUserService(registrations, passwordHash, membershipNumbers);
    }

    private RegisterUserCommand command(Long planId) {
        return new RegisterUserCommand(" User ", " Test ", "cc", "900001", "USER@example.test", "3000000000",
                "correct horse battery staple", planId);
    }
}
