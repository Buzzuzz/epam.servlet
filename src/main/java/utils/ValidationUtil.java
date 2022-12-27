package utils;

import model.dao.DataSource;
import model.dao.impl.UserDAO;

import java.sql.Connection;

import static constants.RegexConstants.*;

/**
 * Util class created specifically for all types of possible validation of data
 */
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
}
