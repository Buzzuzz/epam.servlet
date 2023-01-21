package utils;

import com.servlet.ejournal.model.dao.DataSource;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.exceptions.ValidationError.*;
import static com.servlet.ejournal.utils.ValidationUtil.*;

// TODO : isNewUserValid, isEmailUnique tests
class TestValidationUtil {
    static class TestValidatePassword {
        @Test
        void testNoPasswordUpdate() {
            assertEquals(NONE, validatePassword(""));
        }

        @Test
        void testPasswordMatchesRegex() {
            assertEquals(NONE, validatePassword("123456"));
            assertEquals(NONE, validatePassword("password"));
            assertEquals(NONE, validatePassword("lessThan20sy>*(/"));
        }

        @Test
        void testPasswordDoesntMatchesRegex() {
            assertEquals(PASSWORD, validatePassword("moreThan./34899(*)33030220*$("));
            assertEquals(PASSWORD, validatePassword("somethingWentHorriblyWrong12.44*(#@)$/"));
        }
    }

    static class TestValidateRepeatPassword {
        @Test
        void testPasswordsAreTheSame() {
            assertEquals(NONE, validateRepeatPassword("", ""));
            assertEquals(NONE, validateRepeatPassword("123456", "123456"));
        }

        @Test
        void testPasswordsAreNotTheSame() {
            assertEquals(PASSWORD_REPEAT, validateRepeatPassword("", "1"));
            assertEquals(PASSWORD_REPEAT, validateRepeatPassword("123456pass", "123456pas"));
        }
    }

    static class TestValidatePhoneNumber {
        @Test
        void testValidPhoneNumber() {
            assertEquals(NONE, validatePhoneNumber("123456789"));
        }

        @Test
        void testNotValidPhoneNumber() {
            assertEquals(PHONE_NUMBER, validatePhoneNumber("123456678003"));
            assertEquals(PHONE_NUMBER, validatePhoneNumber("123"));
            assertEquals(PHONE_NUMBER, validatePhoneNumber("48ffj"));
        }
    }

    static class TestValidateEndDate {
        static Timestamp endDate;
        static Timestamp startDate;
        @BeforeAll
        static void setup() {
            endDate = new Timestamp(System.currentTimeMillis() + 1000);
            startDate = new Timestamp(endDate.getTime() - 1000);
        }
        @Test
        void testValidEndDate() {
            assertEquals(NONE, validateEndDate(startDate, endDate));
        }

        @Test
        void testInvalidEndDate() {
            assertEquals(END_DATE, validateEndDate(endDate, startDate));
        }
    }
}
