package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

interface EpsPlanJpaRepository extends JpaRepository<EpsPlanEntity, Long> {
    List<EpsPlanEntity> findAllByActiveTrueOrderByNameAsc();
}
