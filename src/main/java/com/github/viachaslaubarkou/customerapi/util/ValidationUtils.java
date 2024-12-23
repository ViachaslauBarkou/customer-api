package com.github.viachaslaubarkou.customerapi.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?\\d+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private ValidationUtils() {}

    /**
     * Validates a phone number. It should contain only digits and may start with '+'.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Validates an email address for proper format.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
