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
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import co.edu.fcv.training.citas.application.security.RefreshTokenPort;
import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserEntity;
import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenPort refreshTokens;
    private final UserJpaRepository users;
    private final long accessTokenMinutes;

    LoginController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, RefreshTokenPort refreshTokens,
                    UserJpaRepository users,
                    @Value("${app.security.access-token-minutes:15}") long accessTokenMinutes) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokens = refreshTokens;
        this.users = users;
        this.accessTokenMinutes = accessTokenMinutes;
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password()));
        return accessResponse(Long.valueOf(authentication.getName()), authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList(), refreshTokens.issue(Long.valueOf(authentication.getName()), request.deviceInfo()));
    }

    @PostMapping("/refresh")
    LoginResponse refresh(@Valid @RequestBody RefreshRequest request) {
        Long userId = refreshTokens.validUserId(request.refreshToken());
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        UserEntity user = users.findById(userId).filter(UserEntity::active)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        return accessResponse(user.id(), user.roleCodes().stream().map(role -> "ROLE_" + role).toList(), null);
    }

    @PostMapping("/logout")
    void logout(@Valid @RequestBody RefreshRequest request) { refreshTokens.revoke(request.refreshToken()); }

    private LoginResponse accessResponse(Long userId, java.util.List<String> roles, String refreshToken) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder().issuer("citas-api").issuedAt(now)
                .expiresAt(now.plus(accessTokenMinutes, ChronoUnit.MINUTES))
                .subject(userId.toString()).claim("roles", roles)
                .build();
        return new LoginResponse(jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue(), refreshToken, "Bearer", accessTokenMinutes * 60);
    }

    record LoginRequest(@NotBlank @Email String email, @NotBlank String password, String deviceInfo) {}
    record RefreshRequest(@NotBlank String refreshToken) {}
    record LoginResponse(String accessToken, String refreshToken, String tokenType, long expiresInSeconds) {}
}
