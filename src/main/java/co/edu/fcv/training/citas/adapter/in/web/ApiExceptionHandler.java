package co.edu.fcv.training.citas.adapter.in.web;

import co.edu.fcv.training.citas.domain.identity.DuplicateUserException;
import co.edu.fcv.training.citas.domain.identity.InvalidInsurancePlanException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(InvalidInsurancePlanException.class)
    ResponseEntity<ApiError> invalidInsurancePlan() {
        return error(HttpStatus.BAD_REQUEST, "INVALID_INSURANCE_PLAN");
    }

    @ExceptionHandler({DuplicateUserException.class})
    ResponseEntity<ApiError> duplicateUser() {
        return error(HttpStatus.CONFLICT, "DUPLICATE_USER");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalidRequest() {
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code) {
        return ResponseEntity.status(status)
                .body(new ApiError(OffsetDateTime.now(ZoneId.of("America/Bogota")), status.value(), code));
    }

    record ApiError(OffsetDateTime timestamp, int status, String code) {
    }
}
