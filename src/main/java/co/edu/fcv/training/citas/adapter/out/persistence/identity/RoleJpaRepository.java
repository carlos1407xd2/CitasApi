package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RoleJpaRepository extends JpaRepository<RoleEntity, Short> {
    Optional<RoleEntity> findByCode(String code);
}
