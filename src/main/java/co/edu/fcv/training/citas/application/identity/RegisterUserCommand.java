package co.edu.fcv.training.citas.application.identity;

public record RegisterUserCommand(
        String firstName, String lastName, String documentType, String documentNumber,
        String email, String phone, String rawPassword, Long insurancePlanId) {
}
