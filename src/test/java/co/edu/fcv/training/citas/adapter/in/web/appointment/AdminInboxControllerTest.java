package co.edu.fcv.training.citas.adapter.in.web.appointment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdminInboxControllerTest {
    @Test
    void appliesOperationalFiltersAndDoesNotExposePatientName() {
        EntityManager entityManager = mock(EntityManager.class);
        Query appointments = mock(Query.class);
        Query reschedules = mock(Query.class);
        when(entityManager.createNativeQuery(contains("FROM appointments"))).thenReturn(appointments);
        when(entityManager.createNativeQuery(contains("FROM reschedule_requests"))).thenReturn(reschedules);
        when(appointments.setParameter(eq("from"), org.mockito.ArgumentMatchers.any())).thenReturn(appointments);
        when(appointments.setParameter(eq("to"), org.mockito.ArgumentMatchers.any())).thenReturn(appointments);
        when(appointments.setParameter(eq("locationId"), eq(1L))).thenReturn(appointments);
        when(appointments.setParameter(eq("professionalId"), eq(2L))).thenReturn(appointments);
        when(appointments.setParameter(eq("specialtyId"), eq(3L))).thenReturn(appointments);
        when(reschedules.setParameter(eq("from"), org.mockito.ArgumentMatchers.any())).thenReturn(reschedules);
        when(reschedules.setParameter(eq("to"), org.mockito.ArgumentMatchers.any())).thenReturn(reschedules);
        when(reschedules.setParameter(eq("locationId"), eq(1L))).thenReturn(reschedules);
        when(reschedules.setParameter(eq("professionalId"), eq(2L))).thenReturn(reschedules);
        when(reschedules.setParameter(eq("specialtyId"), eq(3L))).thenReturn(reschedules);
        when(appointments.getResultList()).thenReturn(List.<Object[]>of(new Object[]{7L, "APPOINTMENT", "REQUESTED", "Cardio", "HIC", "2026-10-01T08:00"}));
        when(reschedules.getResultList()).thenReturn(List.of());

        var response = new AdminInboxController(entityManager).list(null, null, null, 1L, 2L, 3L);

        assertEquals(1, response.items().size());
        assertFalse(response.items().getFirst().toString().contains("patientName"));
    }
}
