package co.edu.fcv.training.citas.domain.appointment;

public class AppointmentBookingException extends RuntimeException {
    private final String code;

    public AppointmentBookingException(String code) {
        super(code);
        this.code = code;
    }

    public String code() { return code; }
}
