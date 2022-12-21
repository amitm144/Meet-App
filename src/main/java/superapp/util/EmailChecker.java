package superapp.util;

import java.util.regex.*;

public class EmailChecker {

    public static boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
