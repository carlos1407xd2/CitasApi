package co.edu.fcv.training.citas.domain.identity;

public final class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
