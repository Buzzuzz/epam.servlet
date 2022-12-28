package services;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;
import model.entities.UserType;
import utils.PasswordHashUtil;

import java.sql.Connection;
import java.util.Optional;

import static constants.AttributeConstants.*;
import static utils.ValidationUtil.*;

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
                    throw new ServiceException(PASSWORD_ATTR);
                }
            } else {
                log.debug("No user with email " + email);
                throw new ServiceException(EMAIL_ATTR);
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't log in user " + email);
        }
    }

    public static void logOut(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        log.info("User " + user.getEmail() + " logged out");
        req.getSession().invalidate();
    }

    public static boolean signup(HttpServletRequest req) throws ServiceException {
        if (validateNewUser(req)) {
            Connection con = null;
            long generatedId = 0;
            try {
                con = DataSource.getConnection();
                generatedId = dao.save(con, new User(
                        generatedId,
                        req.getParameter(EMAIL_ATTR),
                        PasswordHashUtil.encode(req.getParameter(PASSWORD_ATTR)),
                        req.getParameter(FIRST_NAME),
                        req.getParameter(LAST_NAME),
                        req.getParameter(PHONE_NUMBER),
                        UserType.STUDENT,
                        false,
                        false));
                log.info("User " + generatedId + req.getParameter(EMAIL_ATTR) + " registered successfully!");
                return true;
            } catch (DAOException e) {
                log.error(e.getMessage());
                throw new ServiceException("Can't register new user", e);
            } finally {
                DataSource.close(con);
            }
        }
        return false;
    }

    public static boolean updateUserData() {
        throw new UnsupportedOperationException();
    }
}
