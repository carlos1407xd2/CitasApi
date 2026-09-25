package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentLifecyclePort;
import co.edu.fcv.training.citas.application.appointment.ReschedulePort;
import co.edu.fcv.training.citas.domain.appointment.AppointmentLifecycleException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JpaRescheduleAdapter implements ReschedulePort {
    private static final ZoneId ZONE = ZoneId.of("America/Bogota");
    private final EntityManager entityManager;
    JpaRescheduleAdapter(EntityManager entityManager) { this.entityManager = entityManager; }
    @Override @Transactional
    public Long requestReschedule(Long patient, Long appointmentId, Long locationId, LocalDateTime startAt) {
        if (startAt == null || !startAt.isAfter(LocalDateTime.now(ZONE)) || startAt.getMinute()%30 != 0 || startAt.getSecond()!=0)
            throw new AppointmentLifecycleException("INVALID_RESCHEDULE_TIME");
        Object raw = entityManager.createNativeQuery("""
                SELECT a.professional_id,a.location_id,a.specialty_id,a.scheduled_start_at,a.scheduled_end_at
                FROM appointments a JOIN appointment_statuses st ON st.id=a.status_id
                WHERE a.id=:appointment AND a.patient_user_id=:patient AND st.code='APPROVED' FOR UPDATE
                """).setParameter("appointment",appointmentId).setParameter("patient",patient).getResultList().stream().findFirst().orElse(null);
        if(raw==null) throw new AppointmentLifecycleException("RESCHEDULE_NOT_ALLOWED");
        Object[] a=(Object[])raw; Long professional=((Number)a[0]).longValue(); Long originalLocation=((Number)a[1]).longValue(); Long specialty=((Number)a[2]).longValue(); LocalDateTime oldStart=dateTime(a[3]); LocalDateTime oldEnd=dateTime(a[4]);
        if(((Number)entityManager.createNativeQuery("SELECT COUNT(*) FROM reschedule_requests r JOIN reschedule_request_statuses rs ON rs.id=r.status_id WHERE r.appointment_id=:appointment AND rs.code='PENDING'").setParameter("appointment",appointmentId).getSingleResult()).intValue()>0) throw new AppointmentLifecycleException("RESCHEDULE_ALREADY_PENDING");
        Number duration=(Number)entityManager.createNativeQuery("SELECT appointment_duration_minutes FROM specialties WHERE id=:specialty AND active=TRUE").setParameter("specialty",specialty).getSingleResult();
        LocalDateTime endAt=startAt.plusMinutes(duration.intValue());
        if(((Number)entityManager.createNativeQuery("SELECT COUNT(*) FROM professional_locations WHERE professional_id=:professional AND location_id=:location AND active=TRUE").setParameter("professional",professional).setParameter("location",locationId).getSingleResult()).intValue()!=1) throw new AppointmentLifecycleException("LOCATION_NOT_ASSIGNED");
        List<Tuple> slots=entityManager.createNativeQuery("""
                SELECT ps.id AS id,ps.start_at AS startAt,ps.end_at AS endAt FROM professional_slots ps JOIN availability_blocks ab ON ab.id=ps.availability_block_id
                WHERE ps.appointment_id IS NULL AND ab.active=TRUE AND ab.professional_id=:professional AND ab.location_id=:location AND ps.start_at>=:startAt AND ps.end_at<=:endAt ORDER BY ps.start_at FOR UPDATE
                """,Tuple.class).setParameter("professional",professional).setParameter("location",locationId).setParameter("startAt",startAt).setParameter("endAt",endAt).getResultList();
        int expected=duration.intValue()/30; if(slots.size()!=expected || !consecutive(slots)) throw new AppointmentLifecycleException("SLOT_NOT_AVAILABLE");
        Number pending=(Number)entityManager.createNativeQuery("SELECT id FROM reschedule_request_statuses WHERE code='PENDING'").getSingleResult();
        entityManager.createNativeQuery("""
                INSERT INTO reschedule_requests (appointment_id,requested_by_user_id,requested_location_id,status_id,previous_start_at,previous_end_at,requested_start_at,requested_end_at)
                VALUES (:appointment,:patient,:location,:status,:oldStart,:oldEnd,:newStart,:newEnd)
                """).setParameter("appointment",appointmentId).setParameter("patient",patient).setParameter("location",locationId).setParameter("status",pending).setParameter("oldStart",oldStart).setParameter("oldEnd",oldEnd).setParameter("newStart",startAt).setParameter("newEnd",endAt).executeUpdate();
        Long requestId=((Number)entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
        entityManager.createNativeQuery("UPDATE professional_slots SET appointment_id=:appointment WHERE id IN (:ids)").setParameter("appointment",appointmentId).setParameter("ids",slots.stream().map(x->((Number)x.get("id")).longValue()).toList()).executeUpdate();
        return requestId;
    }

    @Override @Transactional
    public void decideReschedule(Long admin, Long requestId, boolean approve, String reason) {
        Object raw=entityManager.createNativeQuery("""
                SELECT r.appointment_id,r.previous_start_at,r.previous_end_at,r.requested_start_at,r.requested_end_at,rs.code FROM reschedule_requests r JOIN reschedule_request_statuses rs ON rs.id=r.status_id WHERE r.id=:id FOR UPDATE
                """).setParameter("id",requestId).getResultList().stream().findFirst().orElse(null);
        if(raw==null) throw new AppointmentLifecycleException("RESCHEDULE_NOT_FOUND"); Object[] x=(Object[])raw; if(!"PENDING".equals(x[5])) throw new AppointmentLifecycleException("RESCHEDULE_NOT_PENDING");
        Long appointment=((Number)x[0]).longValue(); LocalDateTime oldStart=dateTime(x[1]),oldEnd=dateTime(x[2]),newStart=dateTime(x[3]),newEnd=dateTime(x[4]);
        Number status=(Number)entityManager.createNativeQuery("SELECT id FROM reschedule_request_statuses WHERE code=:code").setParameter("code",approve?"APPROVED":"REJECTED").getSingleResult();
        if(approve) { entityManager.createNativeQuery("UPDATE appointments SET scheduled_start_at=:start,scheduled_end_at=:end WHERE id=:appointment").setParameter("start",newStart).setParameter("end",newEnd).setParameter("appointment",appointment).executeUpdate(); entityManager.createNativeQuery("UPDATE professional_slots SET appointment_id=NULL WHERE appointment_id=:appointment AND start_at>=:oldStart AND end_at<=:oldEnd").setParameter("appointment",appointment).setParameter("oldStart",oldStart).setParameter("oldEnd",oldEnd).executeUpdate(); }
        else entityManager.createNativeQuery("UPDATE professional_slots SET appointment_id=NULL WHERE appointment_id=:appointment AND start_at>=:newStart AND end_at<=:newEnd").setParameter("appointment",appointment).setParameter("newStart",newStart).setParameter("newEnd",newEnd).executeUpdate();
        entityManager.createNativeQuery("UPDATE reschedule_requests SET status_id=:status,decision_reason=:reason,decided_by_user_id=:admin,decided_at=NOW() WHERE id=:id").setParameter("status",status).setParameter("reason",reason).setParameter("admin",admin).setParameter("id",requestId).executeUpdate();
    }
    private boolean consecutive(List<Tuple> slots){for(int i=1;i<slots.size();i++)if(!dateTime(slots.get(i-1).get("endAt")).equals(dateTime(slots.get(i).get("startAt"))))return false;return true;}
    private LocalDateTime dateTime(Object v){if(v instanceof LocalDateTime x)return x;if(v instanceof java.sql.Timestamp x)return x.toLocalDateTime();throw new AppointmentLifecycleException("INVALID_APPOINTMENT_DATA");}
}
