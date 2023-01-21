package com.servlet.ejournal.utils;

import org.junit.jupiter.api.Test;

import static com.servlet.ejournal.utils.PasswordHashUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestPasswordHashUtil {
    private static final String password = "password";

    // Tested before encoded password
    private static final String passwordEncoded = "$argon2i$v=19$m=15360,t=2,p=1$MnsPQCcnd+YEtoF4bZVS1w$o80rVpNg56+Eo0SC7JfKh/iMB3gyq24IzNWo2elkz6I";

    @Test
    void testEncodePassword() {
        assertEquals(96, encode(password).length());
    }

    @Test
    void testComparePassword() {
        assertTrue(verify(passwordEncoded, password));
    }
}
