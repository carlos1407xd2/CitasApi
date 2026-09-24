package co.edu.fcv.training.citas.adapter.in.web.identity;

import co.edu.fcv.training.citas.application.identity.RegisterUserCommand;
import co.edu.fcv.training.citas.application.identity.RegisterUserService;
import co.edu.fcv.training.citas.application.identity.RegisteredUser;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    private final RegisterUserService registerUserService;

    AuthController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        RegisteredUser registered = registerUserService.register(new RegisterUserCommand(
                request.firstName(), request.lastName(), request.documentType(), request.documentNumber(),
                request.email(), request.phone(), request.password(), request.insurancePlanId()));
        return ResponseEntity.created(URI.create("/api/v1/users/" + registered.id()))
                .body(new RegistrationResponse(registered.id(), registered.email(), registered.insurancePlanId()));
    }
}
