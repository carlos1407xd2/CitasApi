package co.edu.fcv.training.citas.application.identity;

public interface UserRegistrationPort {
    RegisteredUser register(PersistedRegistration registration);

    record PersistedRegistration(
            String firstName, String lastName, String documentType, String documentNumber,
            String email, String phone, String passwordHash, Long insurancePlanId,
            String membershipNumber) {
    }
}
