package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Optional;

import static com.servlet.ejournal.exceptions.ValidationError.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;
import static com.servlet.ejournal.utils.ValidationUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestValidationUtil {
    @Nested
    class TestValidatePassword {
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

    @Nested
    class TestValidateRepeatPassword {
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

    @Nested
    class TestValidatePhoneNumber {
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

    @Nested
    class TestValidateEndDate {
        Timestamp endDate = new Timestamp(System.currentTimeMillis() + 1000);
        Timestamp startDate = new Timestamp(endDate.getTime() - 1000);

        @Test
        void testValidEndDate() {
            assertEquals(NONE, validateEndDate(startDate, endDate));
        }

        @Test
        void testInvalidEndDate() {
            assertEquals(END_DATE, validateEndDate(endDate, startDate));
        }
    }

    @Nested
    class TestIsNewUserValid {
        private final String repeatPassword = "password";
        private User user;
        private UserDAO userDaoMock;
        private Connection conMock;

        @BeforeEach
        void setup() {
            user = createTestUser();
            userDaoMock = mock(UserDAO.class);
            conMock = mock(Connection.class);
            when(userDaoMock.getByEmail(conMock, user.getEmail())).thenReturn(Optional.empty());
        }

        @Test
        void testUserInvalidEmail() throws UtilException {
            when(userDaoMock.getByEmail(conMock, user.getEmail())).thenReturn(Optional.of(user));
            assertEquals(EMAIL, isNewUserValid(userDaoMock, conMock, user, repeatPassword));
        }

        @Test
        void testUserInvalidPassword() throws UtilException {
            user.setPassword("1");
            assertEquals(PASSWORD, isNewUserValid(userDaoMock, conMock, user, repeatPassword));
        }

        @Test
        void testUserInvalidPasswordRepeat() throws UtilException {
            assertEquals(PASSWORD_REPEAT, isNewUserValid(userDaoMock, conMock, user, ""));

        }

        @Test
        void testUserInvalidPhone() throws UtilException {
            user.setPhone("");
            assertEquals(PHONE_NUMBER, isNewUserValid(userDaoMock, conMock, user, repeatPassword));
        }

        @Test
        void testUserIsValid() throws UtilException {
            assertEquals(NONE, isNewUserValid(userDaoMock, conMock, user, repeatPassword));
        }
    }

    @Nested
    class TestIsEmailUnique {
        private UserDAO daoMock;
        private Connection conMock;
        private User user;

        @BeforeEach
        void setup() {
            user = createTestUser();
            daoMock = mock(UserDAO.class);
            conMock = mock(Connection.class);
            when(daoMock.getByEmail(conMock, user.getEmail())).thenReturn(Optional.of(user));
        }

        @Test
        void testEmailIsUnique() throws UtilException {
            when(daoMock.getByEmail(conMock, user.getEmail())).thenReturn(Optional.empty());
            assertEquals(NONE, isEmailUnique(daoMock, conMock, user.getEmail()));
        }

        @Test
        void testEmailIsNotUnique() throws UtilException {
            assertEquals(EMAIL, isEmailUnique(daoMock, conMock, user.getEmail()));
        }

        @Test
        void testIsEmailUniqueConnectionIsNull() {
            assertThrows(UtilException.class, () -> isEmailUnique(daoMock, null, user.getEmail()));
        }

        @Test
        void testIsEmailUniqueEmailIsNull() {
            assertThrows(UtilException.class, () -> isEmailUnique(daoMock, conMock, null));
        }
    }
}
