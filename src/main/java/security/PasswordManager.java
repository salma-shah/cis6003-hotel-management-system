package security;

import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

public class PasswordManager {
    // enabling logging in tomcat server
    private static final Logger LOG = Logger.getLogger(PasswordManager.class.getName());

//    // method to generate salt
//    // we will use secure random from the security framework
//    public String generateSalt()
//    {
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[16];  // 16 bytes * 8 = 128 bits
//        random.nextBytes(salt);  // salting the bytes
//        return Base64.getEncoder().encodeToString(salt);
//    }

    // method to salt and hash the password
    // using BCrypt ( which does both )
    public static String saltAndHashPassword(String plainTextPassword)
    {
        String salt = BCrypt.gensalt(12);  // this generates the salt
        return BCrypt.hashpw(plainTextPassword, salt);  // this hashes the password WITH the salt
    }

    // method to verify entered password
    public static boolean checkPassword(String plainTextPassword, String storedPassword)
    {
        // BCypt easily checks if entered password matches the stored password
        return BCrypt.checkpw(plainTextPassword, storedPassword);
    }


}
