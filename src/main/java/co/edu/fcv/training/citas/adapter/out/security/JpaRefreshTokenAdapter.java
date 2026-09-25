package co.edu.fcv.training.citas.adapter.out.security;

import co.edu.fcv.training.citas.application.security.RefreshTokenPort;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaRefreshTokenAdapter implements RefreshTokenPort {
    private static final ZoneId ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    private final long validityDays;
    private final SecureRandom random = new SecureRandom();

    JpaRefreshTokenAdapter(EntityManager entityManager,
                           @Value("${app.security.refresh-token-days:7}") long validityDays) {
        this.entityManager = entityManager;
        this.validityDays = validityDays;
    }

    @Override
    @Transactional
    public String issue(Long userId, String deviceInfo) {
        byte[] bytes = new byte[48];
        random.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        entityManager.createNativeQuery("""
                INSERT INTO refresh_tokens (user_id, token_hash, expires_at, device_info)
                VALUES (:userId, :hash, :expiresAt, :deviceInfo)
                """).setParameter("userId", userId).setParameter("hash", hash(raw))
                .setParameter("expiresAt", LocalDateTime.now(ZONE).plusDays(validityDays))
                .setParameter("deviceInfo", deviceInfo).executeUpdate();
        return raw;
    }

    @Override
    @Transactional(readOnly = true)
    public Long validUserId(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return null;
        Object value = entityManager.createNativeQuery("""
                SELECT user_id FROM refresh_tokens
                WHERE token_hash = :hash AND revoked_at IS NULL AND expires_at > :now
                """).setParameter("hash", hash(rawToken)).setParameter("now", LocalDateTime.now(ZONE))
                .getResultStream().findFirst().orElse(null);
        return value == null ? null : ((Number) value).longValue();
    }

    @Override
    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        entityManager.createNativeQuery("UPDATE refresh_tokens SET revoked_at = :now WHERE token_hash = :hash AND revoked_at IS NULL")
                .setParameter("now", LocalDateTime.now(ZONE)).setParameter("hash", hash(rawToken)).executeUpdate();
    }

    private String hash(String value) {
        try { return Base64.getUrlEncoder().withoutPadding().encodeToString(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception exception) { throw new IllegalStateException("SHA-256 unavailable", exception); }
    }
}
