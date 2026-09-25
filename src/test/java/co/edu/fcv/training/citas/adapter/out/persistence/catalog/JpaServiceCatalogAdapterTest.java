package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JpaServiceCatalogAdapterTest {
    @Mock private LocationJpaRepository locations;
    @Mock private SpecialtyJpaRepository specialties;

    @Test
    void mapsActiveServiceCatalogsWithoutChangingThePersistenceModel() {
        var location = new LocationEntity();
        ReflectionTestUtils.setField(location, "id", (short) 1);
        ReflectionTestUtils.setField(location, "code", "HIC");
        ReflectionTestUtils.setField(location, "name", "Hospital Demo");
        ReflectionTestUtils.setField(location, "address", "Dirección sintética");
        ReflectionTestUtils.setField(location, "city", "Piedecuesta");
        ReflectionTestUtils.setField(location, "department", "Santander");
        var specialty = new SpecialtyEntity();
        ReflectionTestUtils.setField(specialty, "id", (short) 1);
        ReflectionTestUtils.setField(specialty, "code", "MEDICINA_GENERAL");
        ReflectionTestUtils.setField(specialty, "name", "Medicina General");
        ReflectionTestUtils.setField(specialty, "appointmentDurationMinutes", (short) 30);
        ReflectionTestUtils.setField(specialty, "general", true);
        ReflectionTestUtils.setField(specialty, "requiresAdminApproval", false);
        when(locations.findAllByActiveTrueOrderByNameAsc()).thenReturn(List.of(location));
        when(specialties.findAllByActiveTrueOrderByNameAsc()).thenReturn(List.of(specialty));

        var result = new JpaServiceCatalogAdapter(locations, specialties);

        assertThat(result.activeLocations()).extracting("code").containsExactly("HIC");
        assertThat(result.activeSpecialties()).extracting("appointmentDurationMinutes").containsExactly(30);
    }
}
