package co.edu.fcv.training.citas.domain.professional;

public class ProfessionalAdministrationException extends RuntimeException {
    private final String code;
    public ProfessionalAdministrationException(String code) { super(code); this.code = code; }
    public String code() { return code; }
}
