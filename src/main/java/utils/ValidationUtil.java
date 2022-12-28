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
    private static boolean validatePassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    /**
     * Method to validate phone number for specific regex
     *
     * @param phone for validation
     * @return true if check is passed, false otherwise
     */
    private static boolean validatePhoneNumber(String phone) {
        return phone.matches(PHONE_NUMBER_REGEX);
    }

    /**
     * @param pass    Password to compare
     * @param compare Another input to compare with password for equality
     * @return {@link Boolean#TRUE true} if check is passed successful, {@link Boolean#FALSE false} if not
     */
    private static boolean comparePasswords(String pass, String compare) {
        return pass.equals(compare);
    }

    /**
     * Method to check if email is unique (registered).
     *
     * @param email Email to be checked for presence in database
     * @return true if email isn't registered (is unique), false otherwise
     */
    private static boolean checkEmailIsAvailable(String email) {
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
        log.debug("new user validation");
        return validEmail(req, session) && validPassword(req) && validRepeatPassword(req) && validPhoneNumber(req);
    }

    public static boolean validEmail(HttpServletRequest req, HttpSession session) {
        if (!checkEmailIsAvailable(req.getParameter(EMAIL_ATTR))) {
            session.setAttribute(ERROR, EMAIL_ATTR);
            return false;
        }
        return true;
    }

    public static boolean validPassword(HttpServletRequest req) {
        if (req.getParameter(PASSWORD_ATTR).equals("")) {
            return true;
        }
        if (!validatePassword(req.getParameter(PASSWORD_ATTR))) {
            req.getSession().setAttribute(ERROR, PASSWORD_ATTR);
            return false;
        }
        return true;
    }

    public static boolean validRepeatPassword(HttpServletRequest req) {
        if (!comparePasswords(req.getParameter(PASSWORD_REPEAT_ATTR), req.getParameter(PASSWORD_ATTR))) {
            req.getSession().setAttribute(ERROR, PASSWORD_REPEAT_ATTR);
            return false;
        }
        return true;
    }

    public static boolean validPhoneNumber(HttpServletRequest req) {
        if (!validatePhoneNumber(req.getParameter(PHONE_NUMBER))) {
            req.getSession().setAttribute(ERROR, PHONE_NUMBER);
            return false;
        }
        return true;
    }
}
