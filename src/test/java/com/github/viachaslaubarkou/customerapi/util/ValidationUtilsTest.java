package com.github.viachaslaubarkou.customerapi.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    public void testIsValidPhoneNumber() {
        assertTrue(ValidationUtils.isValidPhoneNumber("+1234567890"));
        assertTrue(ValidationUtils.isValidPhoneNumber("1234567890"));
        assertFalse(ValidationUtils.isValidPhoneNumber("123-456"));
        assertFalse(ValidationUtils.isValidPhoneNumber("+abc123"));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("user@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name+label@domain.co.by"));
        assertFalse(ValidationUtils.isValidEmail("user@.com"));
        assertFalse(ValidationUtils.isValidEmail("user@domain,com"));
    }
}
