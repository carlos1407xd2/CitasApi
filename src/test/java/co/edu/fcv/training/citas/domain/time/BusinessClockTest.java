package co.edu.fcv.training.citas.domain.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class BusinessClockTest {

    @Test
    void usesTheBusinessZoneRequiredByTheContract() {
        assertThat(BusinessClock.ZONE).isEqualTo(ZoneId.of("America/Bogota"));
    }
}
