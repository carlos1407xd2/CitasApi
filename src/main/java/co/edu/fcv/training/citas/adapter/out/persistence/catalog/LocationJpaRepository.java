package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findAllByActiveTrueOrderByNameAsc();
}
