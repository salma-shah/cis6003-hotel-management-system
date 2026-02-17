package security;

import java.util.UUID;

public class ReservationIdGenerator implements IdGenerator {
    // we declare the pre fix which should be in front of the reg num
    private static final String RES_PREFIX = "RV";

    // method to generate
    // we use a UUID

    @Override
    public String generateId() {
        return RES_PREFIX + UUID.randomUUID().toString();
    }
}
