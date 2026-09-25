package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, Long> {
    List<SpecialtyEntity> findAllByActiveTrueOrderByNameAsc();
}
