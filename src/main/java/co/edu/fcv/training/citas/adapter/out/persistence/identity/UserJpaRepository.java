package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
}
