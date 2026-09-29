package co.edu.fcv.training.citas.adapter.out.appointment;

import co.edu.fcv.training.citas.application.appointment.AppointmentStatusChangedEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppointmentStatusEventListenerTest {
    @Test
    void writesOnlySafeStatusEventFieldsToOutbox() {
        EntityManager entityManager = mock(EntityManager.class);
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(contains("appointment_status_events"))).thenReturn(query);
        when(query.setParameter(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any())).thenReturn(query);

        new AppointmentStatusEventListener(entityManager).afterCommit(
                new AppointmentStatusChangedEvent(42L, "REQUESTED", "APPROVED", "ADMIN", 9L, Instant.parse("2027-01-15T12:00:00Z")));

        verify(query).setParameter("appointment", 42L);
        verify(query).setParameter("previous", "REQUESTED");
        verify(query).setParameter("newStatus", "APPROVED");
        verify(query).setParameter("source", "ADMIN");
        verify(query).setParameter("actor", 9L);
        verify(query).executeUpdate();
    }
}
