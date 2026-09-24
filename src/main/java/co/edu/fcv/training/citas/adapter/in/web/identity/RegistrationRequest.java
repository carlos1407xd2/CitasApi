package co.edu.fcv.training.citas.adapter.in.web.identity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegistrationRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Size(max = 20) String documentType,
        @NotBlank @Size(max = 40) String documentNumber,
        @NotBlank @Email @Size(max = 160) String email,
        @Size(max = 30) String phone,
        @NotBlank @Size(min = 8, max = 72) String password,
        Long insurancePlanId) {
}
