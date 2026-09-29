package co.edu.fcv.training.citas.adapter.in.web.identity;

import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserEntity;
import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserJpaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController {
    private final UserJpaRepository users;
    ProfileController(UserJpaRepository users) { this.users = users; }
    @GetMapping ProfileResponse get(Authentication authentication) { return response(user(authentication)); }
    @PutMapping ProfileResponse update(Authentication authentication, @Valid @RequestBody UpdateRequest request) {
        UserEntity user = user(authentication); user.updateProfile(request.firstName(), request.lastName(), request.phone()); return response(users.save(user));
    }
    private UserEntity user(Authentication authentication) { return users.findById(Long.valueOf(authentication.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND")); }
    private ProfileResponse response(UserEntity user) { return new ProfileResponse(user.id(), user.firstName(), user.lastName(), user.documentType(), user.documentNumber(), user.email(), user.phone()); }
    record UpdateRequest(@NotBlank String firstName, @NotBlank String lastName, String phone) {}
    record ProfileResponse(Long id, String firstName, String lastName, String documentType, String documentNumber, String email, String phone) {}
}
