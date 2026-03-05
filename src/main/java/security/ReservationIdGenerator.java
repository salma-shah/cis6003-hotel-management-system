package security;

import java.security.SecureRandom;

public class ReservationIdGenerator implements IdGenerator {
    // we declare the pre fix which should be in front of the rew num
    private static final String RES_PREFIX = "RV";
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    // method to generate
    // we use secure random
    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder(10);   // 10 characters long
        for (int i = 0; i < 10; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return RES_PREFIX + sb.toString();
    }
}
