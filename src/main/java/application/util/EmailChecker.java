package application.util;

import java.util.regex.*;

public class EmailChecker {

    public static boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
