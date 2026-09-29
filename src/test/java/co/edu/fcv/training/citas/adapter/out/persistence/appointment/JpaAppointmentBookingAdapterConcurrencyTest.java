package co.edu.fcv.training.citas.adapter.out.persistence.appointment;

import co.edu.fcv.training.citas.application.appointment.BookAppointmentCommand;
import co.edu.fcv.training.citas.domain.appointment.AppointmentBookingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JpaAppointmentBookingAdapterConcurrencyTest {
    @Test
    void locksCandidateSlotsAndRejectsNonConsecutiveDuration() {
        EntityManager entityManager = mock(EntityManager.class);
        var events = mock(org.springframework.context.ApplicationEventPublisher.class);
        Query specialtyQuery = mock(Query.class), offerQuery = mock(Query.class), slotQuery = mock(Query.class);
        Tuple specialty = mock(Tuple.class), firstSlot = mock(Tuple.class), secondSlot = mock(Tuple.class);

        when(entityManager.createNativeQuery(contains("FROM specialties"), eq(Tuple.class))).thenReturn(specialtyQuery);
        when(specialtyQuery.setParameter("id", 1L)).thenReturn(specialtyQuery);
        when(specialtyQuery.getResultList()).thenReturn(List.of(specialty));
        when(specialty.get("duration")).thenReturn(60);
        when(specialty.get("general")).thenReturn(false);
        when(specialty.get("approval")).thenReturn(true);
        when(entityManager.createNativeQuery(contains("COUNT(*) FROM professionals"))).thenReturn(offerQuery);
        when(offerQuery.setParameter(anyString(), any())).thenReturn(offerQuery);
        when(offerQuery.getSingleResult()).thenReturn(1L);
        when(entityManager.createNativeQuery(contains("FROM professional_slots"), eq(Tuple.class))).thenReturn(slotQuery);
        when(slotQuery.setParameter(anyString(), any())).thenReturn(slotQuery);
        when(slotQuery.getResultList()).thenReturn(List.of(firstSlot, secondSlot));
        when(firstSlot.get("id")).thenReturn(11L);
        when(secondSlot.get("id")).thenReturn(12L);
        when(firstSlot.get("startAt")).thenReturn(LocalDateTime.of(2027, 1, 15, 8, 0));
        when(firstSlot.get("endAt")).thenReturn(LocalDateTime.of(2027, 1, 15, 8, 30));
        when(secondSlot.get("startAt")).thenReturn(LocalDateTime.of(2027, 1, 15, 9, 0));
        when(secondSlot.get("endAt")).thenReturn(LocalDateTime.of(2027, 1, 15, 9, 30));

        var adapter = new JpaAppointmentBookingAdapter(entityManager, events);
        assertThatThrownBy(() -> adapter.book(new BookAppointmentCommand(20L, 5L, 1L, 1L, LocalDateTime.of(2027, 1, 15, 8, 0), null)))
                .isInstanceOf(AppointmentBookingException.class).extracting("code").isEqualTo("SLOT_NOT_AVAILABLE");
        verify(entityManager).createNativeQuery(contains("FOR UPDATE"), eq(Tuple.class));
    }
}
