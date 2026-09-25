package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
    java.util.Optional<UserEntity> findByEmailIgnoreCase(String email);
}
