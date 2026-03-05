package security;

import java.util.regex.Pattern;

// class to validate email formats
public class EmailValidation {
    public static boolean validateEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // compiling the regex
        Pattern p = Pattern.compile(emailRegex);

        // checking if email matches the pattern
        return email != null && p.matcher(email).matches();
    }
}
