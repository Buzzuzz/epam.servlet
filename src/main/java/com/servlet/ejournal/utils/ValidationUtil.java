package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.User;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Timestamp;

import static com.servlet.ejournal.constants.RegexConstants.*;
import static com.servlet.ejournal.exceptions.ValidationError.*;

/**
 * Util class created specifically for all types of possible validation of data
 */
@Log4j2
@Setter
public class ValidationUtil {
    // Suppress constructor
    private ValidationUtil() {
    }

    private UserDAO dao = UserDAO.getInstance();

    private static class Holder {
        private static final ValidationUtil util = new ValidationUtil();
    }

    public static ValidationUtil getInstance() {
        return Holder.util;
    }

    /**
     * Method to check if email is unique (not registered in system).
     *
     * @param con   Connection on which to perform check
     * @param email Email to be checked for presence in database
     * @return NONE error {@link ValidationError} if email isn't registered (is unique), EMAIL error otherwise
     */
    public ValidationError isEmailUnique(Connection con, String email) throws UtilException {
        try {
            if (con == null || email == null) throw new DAOException("One of parameters is null!");
            return dao.getByEmail(con, email).isPresent() ? EMAIL : NONE;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new UtilException(e.getMessage(), e);
        }
    }

    /**
     * Method to validate new user before adding data to database
     *
     * @param user           from where to get data for validation
     * @param repeatPassword repeated password from form to validate
     * @return {@link ValidationError} constant with info about error (or it absence)
     */
    public ValidationError isNewUserValid(Connection con, User user, String repeatPassword) throws UtilException {
        if (isEmailUnique(con, user.getEmail()).equals(NONE)) {
            if (validatePassword(user.getPassword()).equals(NONE)) {
                if (validateRepeatPassword(user.getPassword(), repeatPassword).equals(NONE)) {
                    if (validatePhoneNumber(user.getPhone()).equals(NONE)) {
                        return NONE;
                    } else return PHONE_NUMBER;
                } else return PASSWORD_REPEAT;
            } else return PASSWORD;
        } else return EMAIL;
    }

    public static ValidationError validatePassword(String password) {
        if (password.equals("")) {
            return NONE;
        }
        return password.matches(PASSWORD_REGEX) ? NONE : PASSWORD;
    }

    public static ValidationError validateRepeatPassword(String password, String toCompare) {
        return password.equals(toCompare) ? NONE : PASSWORD_REPEAT;
    }

    public static ValidationError validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_REGEX) ? NONE : PHONE_NUMBER;
    }

    public static ValidationError validateEndDate(Timestamp startDate, Timestamp endDate) {
        return startDate.compareTo(endDate) > 0 ? END_DATE : NONE;
    }
}
