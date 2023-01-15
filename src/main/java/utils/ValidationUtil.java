package utils;

import exceptions.ErrorType;
import lombok.extern.log4j.Log4j2;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Comparator;

import static constants.RegexConstants.*;
import static exceptions.ErrorType.*;

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
    private static ErrorType isEmailUnique(String email) {
        UserDAO dao = UserDAO.getInstance();
        Connection con = null;
        try {
            con = DataSource.getConnection();
            if (dao.getByEmail(con, email).isPresent()) {
                return EMAIL;
            }
        } finally {
            DataSource.close(con);
        }
        return ErrorType.NONE;
    }

    /**
     * Method to validate new user before adding data to database
     *
     * @param user           from where to get data for validation
     * @param repeatPassword repeated password from form to validate
     * @return {@link ErrorType} constant with info about error (or it absence)
     */
    public static ErrorType isNewUserValid(User user, String repeatPassword) {
        if (isEmailUnique(user.getEmail()).equals(NONE)) {
            if (validatePassword(user.getPassword()).equals(NONE)) {
                if (validateRepeatPassword(user.getPassword(), repeatPassword).equals(NONE)) {
                    if (validatePhoneNumber(user.getPhone()).equals(NONE)) {
                        return NONE;
                    } else return ErrorType.PHONE_NUMBER;
                } else return PASSWORD_REPEAT;
            } else return PASSWORD;
        } else return EMAIL;
    }

    public static ErrorType validatePassword(String password) {
        if (password.equals("")) {
            return NONE;
        }
        return password.matches(PASSWORD_REGEX) ? NONE : PASSWORD;
    }

    public static ErrorType validateRepeatPassword(String password, String toCompare) {
        return password.equals(toCompare) ? NONE : PASSWORD_REPEAT;
    }

    public static ErrorType validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_REGEX) ? NONE : PHONE_NUMBER;
    }

    public static ErrorType validateEndDate(Timestamp startDate, Timestamp endDate) {
        return startDate.compareTo(endDate) > 0 ? END_DATE : NONE;
    }
}
