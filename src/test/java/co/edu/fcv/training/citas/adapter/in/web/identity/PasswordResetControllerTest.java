package co.edu.fcv.training.citas.adapter.in.web.identity;

import co.edu.fcv.training.citas.adapter.out.persistence.identity.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordResetControllerTest {
    @Test
    void doesNotEnumerateUnknownEmailAndRejectsExpiredOrUsedToken() {
        UserJpaRepository users = mock(UserJpaRepository.class);
        EntityManager entityManager = mock(EntityManager.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(users.findByEmailIgnoreCase("unknown@example.test")).thenReturn(Optional.empty());
        Query tokenQuery = mock(Query.class);
        when(entityManager.createNativeQuery(contains("SELECT user_id FROM password_reset_tokens"))).thenReturn(tokenQuery);
        when(tokenQuery.setParameter(eq("hash"), org.mockito.ArgumentMatchers.anyString())).thenReturn(tokenQuery);
        when(tokenQuery.getResultList()).thenReturn(List.of());

        var controller = new PasswordResetController(users, entityManager, encoder);
        assertThat(controller.request(new PasswordResetController.Request("unknown@example.test")).getBody().developmentToken()).isNull();
        assertThat(controller.confirm(new PasswordResetController.Confirm("invalid-token", "NewPassword123*")).getStatusCode().value()).isEqualTo(400);
    }
}
