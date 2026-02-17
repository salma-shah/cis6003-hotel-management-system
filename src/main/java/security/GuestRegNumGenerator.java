package security;

import java.util.UUID;

public class GuestRegNumGenerator implements IdGenerator {

    // we declare the pre fix which should be in front of the reg num
    private static final String GUEST_PREFIX = "OV";
   // private static final String RES_PREFIX = "RV";

    // method to generate
    // we use a UUID
    public static String generateMembershipID(){
        return GUEST_PREFIX + UUID.randomUUID().toString();
    }
  //  public static String generateReservationID(){
//        return RES_PREFIX + UUID.randomUUID().toString();
//    }

    @Override
    public String generateId() {
        return GUEST_PREFIX + UUID.randomUUID().toString();
    }
}
