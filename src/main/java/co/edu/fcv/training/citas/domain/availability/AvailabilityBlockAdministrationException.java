package co.edu.fcv.training.citas.domain.availability;

public class AvailabilityBlockAdministrationException extends RuntimeException {
    private final String code;

    public AvailabilityBlockAdministrationException(String code) {
        super(code);
        this.code = code;
    }

    public String code() { return code; }
}
