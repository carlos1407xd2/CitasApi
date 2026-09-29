package co.edu.fcv.training.citas.adapter.out.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class AppointmentStatusEventListener {
    private static final Logger log = LoggerFactory.getLogger(AppointmentStatusEventListener.class);
    private final EntityManager entityManager;
    AppointmentStatusEventListener(EntityManager entityManager) { this.entityManager = entityManager; }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    void afterCommit(AppointmentStatusChangedEvent event) {
        entityManager.createNativeQuery("INSERT INTO appointment_status_events (appointment_id,previous_status,new_status,source,actor_user_id,occurred_at) VALUES (:appointment,:previous,:newStatus,:source,:actor,:occurred)")
                .setParameter("appointment", event.appointmentId()).setParameter("previous", event.previousStatus())
                .setParameter("newStatus", event.newStatus()).setParameter("source", event.source())
                .setParameter("actor", event.actorUserId()).setParameter("occurred", java.sql.Timestamp.from(event.occurredAt())).executeUpdate();
        log.info("AppointmentStatusChanged appointmentId={} previousStatus={} newStatus={} source={} actorUserId={} occurredAt={}",
                event.appointmentId(), event.previousStatus(), event.newStatus(), event.source(), event.actorUserId(), event.occurredAt());
    }
}
