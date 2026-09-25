package co.edu.fcv.training.citas.domain.time;

import java.time.ZoneId;

/** Business-time policy shared by appointment use cases. */
public final class BusinessClock {

    public static final ZoneId ZONE = ZoneId.of("America/Bogota");

    private BusinessClock() {
    }
}
