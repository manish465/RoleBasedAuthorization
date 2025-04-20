package com.manish.common.util;

public class RegexpUtil {
    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEXP);
    }

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_REGEXP);
    }

}
