package co.edu.fcv.training.citas.application.identity;

import java.util.Locale;

/** Application use case; persistence and cryptography remain behind ports. */
public final class RegisterUserService {

    private final UserRegistrationPort userRegistrationPort;
    private final PasswordHashPort passwordHashPort;
    private final MembershipNumberPort membershipNumberPort;

    public RegisterUserService(UserRegistrationPort userRegistrationPort, PasswordHashPort passwordHashPort,
            MembershipNumberPort membershipNumberPort) {
        this.userRegistrationPort = userRegistrationPort;
        this.passwordHashPort = passwordHashPort;
        this.membershipNumberPort = membershipNumberPort;
    }

    public RegisteredUser register(RegisterUserCommand command) {
        Long planId = command.insurancePlanId();
        String membershipNumber = planId == null ? null : membershipNumberPort.next();
        return userRegistrationPort.register(new UserRegistrationPort.PersistedRegistration(
                command.firstName().trim(), command.lastName().trim(),
                command.documentType().trim().toUpperCase(Locale.ROOT), command.documentNumber().trim(),
                command.email().trim().toLowerCase(Locale.ROOT),
                command.phone() == null ? null : command.phone().trim(),
                passwordHashPort.hash(command.rawPassword()), planId, membershipNumber));
    }
}
