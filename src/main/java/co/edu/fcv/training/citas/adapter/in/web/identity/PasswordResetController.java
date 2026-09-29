package co.edu.fcv.training.citas.adapter.in.web.identity;

import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/password")
class PasswordResetController {
    private final UserJpaRepository users;
    private final EntityManager entityManager;
    private final PasswordEncoder encoder;
    private final SecureRandom random = new SecureRandom();

    PasswordResetController(UserJpaRepository users, EntityManager entityManager, PasswordEncoder encoder) {
        this.users = users; this.entityManager = entityManager; this.encoder = encoder;
    }

    @PostMapping("/request")
    @Transactional
    ResponseEntity<RequestResponse> request(@Valid @RequestBody Request request) {
        var user = users.findByEmailIgnoreCase(request.email());
        if (user.isEmpty()) return ResponseEntity.ok(new RequestResponse(null));
        String raw = UUID.randomUUID() + Long.toHexString(random.nextLong());
        String hash = hash(raw);
        entityManager.createNativeQuery("INSERT INTO password_reset_tokens (user_id, token_hash, expires_at, used_at) VALUES (:user,:hash,:expires,NULL)")
                .setParameter("user", user.get().id()).setParameter("hash", hash)
                .setParameter("expires", java.sql.Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES))).executeUpdate();
        return ResponseEntity.ok(new RequestResponse(raw));
    }

    @PostMapping("/confirm")
    @Transactional
    ResponseEntity<Void> confirm(@Valid @RequestBody Confirm request) {
        String hash = hash(request.token());
        var rows = entityManager.createNativeQuery("SELECT user_id FROM password_reset_tokens WHERE token_hash=:hash AND used_at IS NULL AND expires_at > NOW()")
                .setParameter("hash", hash).getResultList();
        if (rows.isEmpty()) return ResponseEntity.badRequest().build();
        Long userId = ((Number) rows.getFirst()).longValue();
        entityManager.createNativeQuery("UPDATE users SET password_hash=:password WHERE id=:id").setParameter("password", encoder.encode(request.password())).setParameter("id", userId).executeUpdate();
        entityManager.createNativeQuery("UPDATE password_reset_tokens SET used_at=NOW() WHERE token_hash=:hash").setParameter("hash", hash).executeUpdate();
        entityManager.createNativeQuery("UPDATE refresh_tokens SET revoked_at=NOW() WHERE user_id=:id AND revoked_at IS NULL").setParameter("id", userId).executeUpdate();
        return ResponseEntity.noContent().build();
    }

    private String hash(String raw) { try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8))); } catch (Exception e) { throw new IllegalStateException(e); } }
    record Request(@Email @NotBlank String email) {}
    record Confirm(@NotBlank String token, @NotBlank String password) {}
    record RequestResponse(String developmentToken) {}
}
