package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.dao.DataSource;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Timestamp;

import static com.servlet.ejournal.constants.RegexConstants.*;

/**
 * Util class created specifically for all types of possible validation of data
 */
@Log4j2
public class ValidationUtil {
    // Suppress constructor
    private ValidationUtil() {
    }

    /**
     * Method to check if email is unique (not registered in system).
     *
     * @param email Email to be checked for presence in database
     * @return "email" error {@link String} if email isn't registered (is unique), "none" error {@link String} otherwise
     */
    private static ValidationError isEmailUnique(String email) {
        UserDAO dao = UserDAO.getInstance();
        Connection con = null;
        try {
            con = DataSource.getConnection();
            if (dao.getByEmail(con, email).isPresent()) {
                return ValidationError.EMAIL;
            }
        } finally {
            DataSource.close(con);
        }
        return ValidationError.NONE;
    }

    /**
     * Method to validate new user before adding data to database
     *
     * @param user           from where to get data for validation
     * @param repeatPassword repeated password from form to validate
     * @return {@link ValidationError} constant with info about error (or it absence)
     */
    public static ValidationError isNewUserValid(User user, String repeatPassword) {
        if (isEmailUnique(user.getEmail()).equals(ValidationError.NONE)) {
            if (validatePassword(user.getPassword()).equals(ValidationError.NONE)) {
                if (validateRepeatPassword(user.getPassword(), repeatPassword).equals(ValidationError.NONE)) {
                    if (validatePhoneNumber(user.getPhone()).equals(ValidationError.NONE)) {
                        return ValidationError.NONE;
                    } else return ValidationError.PHONE_NUMBER;
                } else return ValidationError.PASSWORD_REPEAT;
            } else return ValidationError.PASSWORD;
        } else return ValidationError.EMAIL;
    }

    public static ValidationError validatePassword(String password) {
        if (password.equals("")) {
            return ValidationError.NONE;
        }
        return password.matches(PASSWORD_REGEX) ? ValidationError.NONE : ValidationError.PASSWORD;
    }

    public static ValidationError validateRepeatPassword(String password, String toCompare) {
        return password.equals(toCompare) ? ValidationError.NONE : ValidationError.PASSWORD_REPEAT;
    }

    public static ValidationError validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_REGEX) ? ValidationError.NONE : ValidationError.PHONE_NUMBER;
    }

    public static ValidationError validateEndDate(Timestamp startDate, Timestamp endDate) {
        return startDate.compareTo(endDate) > 0 ? ValidationError.END_DATE : ValidationError.NONE;
    }
}
