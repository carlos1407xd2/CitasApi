package co.edu.fcv.training.citas.domain.appointment;

public class AppointmentLifecycleException extends RuntimeException {
    private final String code;
    public AppointmentLifecycleException(String code) { super(code); this.code = code; }
    public String code() { return code; }
}
