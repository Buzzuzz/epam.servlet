package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import model.dao.DataSource;
import model.dao.impl.UserDAO;

import java.sql.Connection;

import static constants.AttributeConstants.*;
import static constants.AttributeConstants.PHONE_NUMBER;
import static constants.RegexConstants.*;

/**
 * Util class created specifically for all types of possible validation of data
 */
@Log4j2
public class ValidationUtil {
    // Suppress constructor
    private ValidationUtil() {
    }

    /**
     * Method to validate password for specific regex
     *
     * @param password for validation
     * @return {@link Boolean#TRUE true} if check is passed successful, {@link Boolean#FALSE false} if not
     */
    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    /**
     * Method to validate phone number for specific regex
     *
     * @param phone for validation
     * @return true if check is passed, false otherwise
     */
    public static boolean validatePhoneNumber(String phone) {
        return phone.matches(PHONE_NUMBER_REGEX);
    }

    /**
     * @param pass    Password to compare
     * @param compare Another input to compare with password for equality
     * @return {@link Boolean#TRUE true} if check is passed successful, {@link Boolean#FALSE false} if not
     */
    public static boolean comparePasswords(String pass, String compare) {
        return pass.equals(compare);
    }

    /**
     * Method to check if email is unique (registered).
     *
     * @param email Email to be checked for presence in database
     * @return true if email isn't registered (is unique), false otherwise
     */
    public static boolean checkEmailIsAvailable(String email) {
        UserDAO dao = UserDAO.getInstance();
        Connection con = null;
        try {
            con = DataSource.getConnection();
            if (dao.getByEmail(con, email).isPresent()) {
                return false;
            }
        } finally {
            DataSource.close(con);
        }
        return true;
    }

    /**
     * Method to validate new user before adding data to database
     *
     * @param req from where to get new user data
     * @return true if all checks are good, false otherwise
     */
    public static boolean validateNewUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (!checkEmailIsAvailable((String) req.getParameter(EMAIL_ATTR))) {
            session.setAttribute(ERROR, EMAIL_ATTR);
            return false;
        }
        log.debug("email pass");
        if (!validatePassword((String) req.getParameter(PASSWORD_ATTR))) {
            session.setAttribute(ERROR, PASSWORD_ATTR);
            return false;
        }
        log.debug("valid password");
        if (!comparePasswords((String) req.getParameter(PASSWORD_REPEAT_ATTR), (String) req.getParameter(PASSWORD_ATTR))) {
            session.setAttribute(ERROR, PASSWORD_REPEAT_ATTR);
            return false;
        }
        log.debug("passwords are equal");
        if (!validatePhoneNumber((String) req.getParameter(PHONE_NUMBER))) {
            session.setAttribute(ERROR, PHONE_NUMBER);
            return false;
        }
        log.debug("phone is valid");
        return true;
    }
}
