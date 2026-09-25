package co.edu.fcv.training.citas.adapter.out.persistence.availability;

import co.edu.fcv.training.citas.application.availability.AvailabilityBlockAdministrationPort;
import co.edu.fcv.training.citas.domain.availability.AvailabilityBlockAdministrationException;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaAvailabilityBlockAdministrationAdapter implements AvailabilityBlockAdministrationPort {
    private final EntityManager entityManager;

    JpaAvailabilityBlockAdministrationAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Block> list(Long userId) {
        Long professionalId = professionalId(userId);
        return entityManager.createNativeQuery("""
                SELECT id, professional_id, location_id, available_date, start_time, end_time, active
                FROM availability_blocks WHERE professional_id = :professional
                ORDER BY available_date, start_time, id
                """).setParameter("professional", professionalId).getResultList().stream()
                .map(row -> toBlock((Object[]) row)).toList();
    }

    @Override
    @Transactional
    public Block create(Long userId, CreateBlock command) {
        Long professionalId = professionalId(userId);
        validate(command.locationId(), command.date(), command.startTime(), command.endTime());
        ensureLocation(professionalId, command.locationId());
        ensureNoOverlap(professionalId, command.locationId(), command.date(), command.startTime(), command.endTime(), null);
        entityManager.createNativeQuery("""
                INSERT INTO availability_blocks (professional_id, location_id, available_date, start_time, end_time, active)
                VALUES (:professional, :location, :date, :start, :end, TRUE)
                """).setParameter("professional", professionalId).setParameter("location", command.locationId())
                .setParameter("date", command.date()).setParameter("start", command.startTime())
                .setParameter("end", command.endTime()).executeUpdate();
        Long blockId = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
        createSlots(blockId, command.date(), command.startTime(), command.endTime());
        return new Block(blockId, professionalId, command.locationId(), command.date(), command.startTime(), command.endTime(), true);
    }

    @Override
    @Transactional
    public Block update(Long userId, Long blockId, UpdateBlock command) {
        Long professionalId = professionalId(userId);
        Block current = findOwned(professionalId, blockId);
        if (hasBookedSlots(blockId)) throw new AvailabilityBlockAdministrationException("BLOCK_HAS_APPOINTMENTS");
        LocalDate date = command.date() == null ? current.date() : command.date();
        LocalTime start = command.startTime() == null ? current.startTime() : command.startTime();
        LocalTime end = command.endTime() == null ? current.endTime() : command.endTime();
        Long location = command.locationId() == null ? current.locationId() : command.locationId();
        validate(location, date, start, end);
        ensureLocation(professionalId, location);
        ensureNoOverlap(professionalId, location, date, start, end, blockId);
        boolean active = command.active() == null ? current.active() : command.active();
        entityManager.createNativeQuery("""
                UPDATE availability_blocks SET location_id=:location, available_date=:date,
                start_time=:start, end_time=:end, active=:active WHERE id=:id AND professional_id=:professional
                """).setParameter("location", location).setParameter("date", date).setParameter("start", start)
                .setParameter("end", end).setParameter("active", active).setParameter("id", blockId)
                .setParameter("professional", professionalId).executeUpdate();
        entityManager.createNativeQuery("DELETE FROM professional_slots WHERE availability_block_id=:block")
                .setParameter("block", blockId).executeUpdate();
        if (active) createSlots(blockId, date, start, end);
        return new Block(blockId, professionalId, location, date, start, end, active);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long blockId) {
        Long professionalId = professionalId(userId);
        findOwned(professionalId, blockId);
        if (hasBookedSlots(blockId)) throw new AvailabilityBlockAdministrationException("BLOCK_HAS_APPOINTMENTS");
        entityManager.createNativeQuery("DELETE FROM professional_slots WHERE availability_block_id=:block")
                .setParameter("block", blockId).executeUpdate();
        entityManager.createNativeQuery("DELETE FROM availability_blocks WHERE id=:id AND professional_id=:professional")
                .setParameter("id", blockId).setParameter("professional", professionalId).executeUpdate();
    }

    private void validate(Long location, LocalDate date, LocalTime start, LocalTime end) {
        if (location == null || date == null || start == null || end == null) throw new AvailabilityBlockAdministrationException("INVALID_BLOCK");
        if (date.isBefore(LocalDate.now()) || !end.isAfter(start) || !onHalfHour(start) || !onHalfHour(end))
            throw new AvailabilityBlockAdministrationException("INVALID_BLOCK_TIME");
    }

    private boolean onHalfHour(LocalTime time) { return time.getSecond() == 0 && time.getNano() == 0 && time.getMinute() % 30 == 0; }

    private void ensureLocation(Long professionalId, Long locationId) {
        Number count = ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM professional_locations WHERE professional_id=:professional AND location_id=:location AND active=TRUE")
                .setParameter("professional", professionalId).setParameter("location", locationId).getSingleResult());
        if (count.intValue() != 1) throw new AvailabilityBlockAdministrationException("LOCATION_NOT_ASSIGNED");
    }

    private void ensureNoOverlap(Long professionalId, Long locationId, LocalDate date, LocalTime start, LocalTime end, Long excludedId) {
        var query = entityManager.createNativeQuery("""
                SELECT COUNT(*) FROM availability_blocks WHERE professional_id=:professional AND location_id=:location
                AND available_date=:date AND active=TRUE AND start_time < :end AND end_time > :start
                AND (:excluded IS NULL OR id <> :excluded)
                """).setParameter("professional", professionalId).setParameter("location", locationId)
                .setParameter("date", date).setParameter("start", start).setParameter("end", end).setParameter("excluded", excludedId);
        if (((Number) query.getSingleResult()).intValue() > 0) throw new AvailabilityBlockAdministrationException("BLOCK_OVERLAP");
    }

    private void createSlots(Long blockId, LocalDate date, LocalTime start, LocalTime end) {
        List<Object[]> rows = new ArrayList<>();
        for (LocalTime cursor = start; cursor.isBefore(end); cursor = cursor.plusMinutes(30)) {
            rows.add(new Object[]{LocalDateTime.of(date, cursor), LocalDateTime.of(date, cursor.plusMinutes(30))});
        }
        for (Object[] row : rows) entityManager.createNativeQuery("INSERT INTO professional_slots (availability_block_id,start_at,end_at) VALUES (:block,:start,:end)")
                .setParameter("block", blockId).setParameter("start", row[0]).setParameter("end", row[1]).executeUpdate();
    }

    private Block findOwned(Long professionalId, Long blockId) {
        Object result = entityManager.createNativeQuery("SELECT id,professional_id,location_id,available_date,start_time,end_time,active FROM availability_blocks WHERE id=:id AND professional_id=:professional")
                .setParameter("id", blockId).setParameter("professional", professionalId).getResultList().stream().findFirst().orElse(null);
        if (result == null) throw new AvailabilityBlockAdministrationException("BLOCK_NOT_FOUND");
        Object[] row = (Object[]) result;
        return toBlock(row);
    }

    private boolean hasBookedSlots(Long blockId) { return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM professional_slots WHERE availability_block_id=:block AND appointment_id IS NOT NULL")
            .setParameter("block", blockId).getSingleResult()).intValue() > 0; }
    private Long professionalId(Long userId) {
        Object result = entityManager.createNativeQuery("SELECT id FROM professionals WHERE user_id=:user AND active=TRUE").setParameter("user", userId).getResultList().stream().findFirst().orElse(null);
        if (result == null) throw new AvailabilityBlockAdministrationException("PROFESSIONAL_NOT_FOUND");
        return ((Number) result).longValue();
    }
    private Block toBlock(Object[] row) { return new Block(((Number) row[0]).longValue(), ((Number) row[1]).longValue(), ((Number) row[2]).longValue(), ((java.sql.Date) row[3]).toLocalDate(), ((java.sql.Time) row[4]).toLocalTime(), ((java.sql.Time) row[5]).toLocalTime(), (Boolean) row[6]); }
}
