package co.edu.fcv.training.citas.application.security;

public interface RefreshTokenPort {
    String issue(Long userId, String deviceInfo);
    Long validUserId(String rawToken);
    void revoke(String rawToken);
}
