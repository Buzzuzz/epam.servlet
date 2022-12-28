package services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.dao.DAOException;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;
import utils.PasswordHashUtil;

import java.sql.Connection;
import java.util.Optional;

import static constants.AttributeConstants.*;

@Log4j2
public class UserService {
    private static final UserDAO dao = UserDAO.getInstance();

    // Suppress constructor
    private UserService() {
    }

    public static User logIn(String email, String password) throws ServiceException {
        try {
            Connection con = DataSource.getConnection();
            Optional<User> daoResult = dao.getByEmail(con, email);
            DataSource.close(con);

            if (daoResult.isPresent()) {
                User user = daoResult.get();
                if (PasswordHashUtil.verify(user.getPassword(), password)) {
                    log.info("User " + user.getEmail() + " logged in successful!");
                    return user;
                } else {
                    log.debug("Password doesn't match! " + user.getEmail());
                }
            } else {
                log.debug("No user with email " + email);
                throw new ServiceException("Can't login user " + email);
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't log in user " + email);
        }
        return null;
    }

    public static void logOut(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        log.info("User " + user.getEmail() + " logged out");
        req.getSession().invalidate();
    }

    public static boolean signup() {
        throw new UnsupportedOperationException();
    }
}
