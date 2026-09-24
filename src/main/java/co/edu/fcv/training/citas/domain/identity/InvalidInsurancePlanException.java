package co.edu.fcv.training.citas.domain.identity;

public final class InvalidInsurancePlanException extends RuntimeException {
    public InvalidInsurancePlanException() {
        super("insurancePlanId must identify an active plan");
    }
}
