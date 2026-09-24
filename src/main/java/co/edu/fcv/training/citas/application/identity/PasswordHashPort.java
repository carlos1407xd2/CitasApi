package co.edu.fcv.training.citas.application.identity;

public interface PasswordHashPort {
    String hash(String rawPassword);
}
