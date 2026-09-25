package co.edu.fcv.training.citas.adapter.in.web.identity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final long accessTokenMinutes;

    LoginController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                    @Value("${app.security.access-token-minutes:15}") long accessTokenMinutes) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.accessTokenMinutes = accessTokenMinutes;
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password()));
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder().issuer("citas-api").issuedAt(now)
                .expiresAt(now.plus(accessTokenMinutes, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .build();
        return new LoginResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), "Bearer", accessTokenMinutes * 60);
    }

    record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    record LoginResponse(String accessToken, String tokenType, long expiresInSeconds) {}
}
