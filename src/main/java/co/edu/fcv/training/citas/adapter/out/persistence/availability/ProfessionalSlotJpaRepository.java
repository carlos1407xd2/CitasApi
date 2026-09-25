package co.edu.fcv.training.citas.adapter.out.persistence.availability;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class ProfessionalSlotJpaRepository {
    private final EntityManager entityManager;
    ProfessionalSlotJpaRepository(EntityManager entityManager) { this.entityManager = entityManager; }

    List<FreeSlotProjection> findFreeSlots(LocalDate date, Short locationId, Short specialtyId, Long professionalId) {
        List<Tuple> rows = entityManager.createNativeQuery("""
                SELECT ps.id AS slotId, ps.start_at AS startAt, ps.end_at AS endAt,
                       ab.professional_id AS professionalId, ab.location_id AS locationId
                FROM professional_slots ps
                JOIN availability_blocks ab ON ab.id = ps.availability_block_id
                JOIN professionals p ON p.id = ab.professional_id AND p.active = TRUE
                JOIN locations l ON l.id = ab.location_id AND l.active = TRUE
                LEFT JOIN professional_specialties psp
                  ON psp.professional_id = ab.professional_id AND psp.active = TRUE
                LEFT JOIN specialties s
                  ON s.id = psp.specialty_id AND s.active = TRUE
                WHERE ps.appointment_id IS NULL
                  AND ab.active = TRUE
                  AND ab.available_date = :date
                  AND (:locationId IS NULL OR ab.location_id = :locationId)
                  AND (:professionalId IS NULL OR ab.professional_id = :professionalId)
                  AND (:specialtyId IS NULL OR s.id = :specialtyId)
                ORDER BY ps.start_at, ab.professional_id, ab.location_id
                """, Tuple.class)
                .setParameter("date", date)
                .setParameter("locationId", locationId)
                .setParameter("specialtyId", specialtyId)
                .setParameter("professionalId", professionalId)
                .getResultList();
        return rows.stream()
                .map(x -> new FreeSlotProjection(
                        ((Number) x.get("slotId")).longValue(),
                        ((Number) x.get("professionalId")).longValue(),
                        ((Number) x.get("locationId")).longValue(),
                        ((java.sql.Timestamp) x.get("startAt")).toLocalDateTime(),
                        ((java.sql.Timestamp) x.get("endAt")).toLocalDateTime()))
                .toList();
    }
}
