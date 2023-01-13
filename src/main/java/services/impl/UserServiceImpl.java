package services.impl;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;
import model.entities.UserType;
import services.UserService;
import services.dto.UserDTO;
import utils.CountRecordsUtil;
import utils.PasswordHashUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;
import static model.dao.DataSource.*;
import static utils.ValidationUtil.*;

@Log4j2
public class UserServiceImpl implements UserService {
    private static final UserDAO dao = UserDAO.getInstance();

    private static class Holder {
        private static final UserService service = new UserServiceImpl();
    }

    // Suppress constructor
    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        return Holder.service;
    }

    public User logIn(String email, String password) throws ServiceException {
        try {
            Connection con = DataSource.getConnection();
            Optional<User> daoResult = dao.getByEmail(con, email);
            close(con);

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

    public void logOut(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        log.info("User " + user.getEmail() + " logged out");
        req.getSession().invalidate();
    }

    public long signUp(HttpServletRequest req) throws ServiceException {
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
                log.info("User " + generatedId + " " + req.getParameter(EMAIL_ATTR) + " registered successfully!");
                return generatedId;
            } catch (DAOException e) {
                log.error(e.getMessage());
                throw new ServiceException("Can't register new user", e);
            } finally {
                close(con);
            }
        }
        throw new ServiceException("Provided data is not valid!");
    }

    public boolean updateUserData(HttpServletRequest req) throws ServiceException {
        Connection con = null;
        User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        try {
            con = DataSource.getConnection();
            if (validPassword(req) && validRepeatPassword(req) && validPhoneNumber(req)) {
                user.setFirst_name(req.getParameter(FIRST_NAME));
                user.setLast_name(req.getParameter(LAST_NAME));
                user.setPassword(req.getParameter(PASSWORD_ATTR).equals("") ? user.getPassword() :
                        PasswordHashUtil.encode(req.getParameter(PASSWORD_ATTR)));
                user.setPhone(req.getParameter(PHONE_NUMBER));
                dao.update(con, user);
                req.getSession().setAttribute(ERROR, null);
                log.debug("User " + user.getEmail() + " updated successful");
                return true;
            }
            return false;
        } catch (DAOException e) {
            log.error("Can't update user " + user.getEmail());
            throw new ServiceException("Can't update user " + user.getEmail());
        } finally {
            close(con);
        }
    }

    @Override
    public UserDTO getUserDTO(User user) {
        Connection con = null;

        try {
            con = getConnection();

            return new UserDTO(
                    user.getU_id(),
                    user.getEmail(),
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getUser_type().name(),
                    String.valueOf(user.is_blocked()),
                    String.valueOf(user.isSend_notification())
            );
        } finally {
            close(con);
        }
    }

    @Override
    public List<UserDTO> getAllUsers(HttpServletRequest req) {
        Connection con = null;

        try {
            con = getConnection();
            int limit, currentPage, offset;
            String sorting;

            limit = req.getParameter(DISPLAY_RECORDS_NUMBER) == null ?
                    DEFAULT_LIMIT :
                    Integer.parseInt(req.getParameter(DISPLAY_RECORDS_NUMBER));

            currentPage = req.getParameter(CURRENT_PAGE) == null ?
                    DEFAULT_PAGE :
                    Integer.parseInt(req.getParameter(CURRENT_PAGE));

            offset = limit * (currentPage - 1);

            sorting = req.getParameter(SORTING_TYPE) == null ?
                    DEFAULT_USER_SORTING :
                    req.getParameter(SORTING_TYPE);

            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, CountRecordsUtil.getPages(limit, getUserCount()));

            List<User> userList = (List<User>) dao.getAll(con, limit, offset, sorting);
            return userList
                    .stream()
                    .map(this::getUserDTO)
                    .collect(Collectors.toCollection(ArrayList::new));
        } finally {
            close(con);
        }
    }

    @Override
    public long deleteUser(long id) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return dao.delete(con, id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            close(con);
        }
    }

    @Override
    public long changeUserLockStatus(long id, boolean status) throws ServiceException {
        Connection con = null;
        long[] generated = new long[1];
        try {
            con = getConnection();
            Connection finalCon = con;
            dao.get(con, id).ifPresent(user -> {
                user.set_blocked(status);
                generated[0] = dao.update(finalCon, user);
            });
        } catch (Exception e) {
            log.error("Can't lock user: " + id, e);
            throw new ServiceException("Can't lock user: " + id, e);
        } finally {
            close(con);
        }
        return generated[0];
    }

    @Override
    public Optional<User> getUser(long id) {
        Connection con = null;
        try {
            con = getConnection();
            return dao.get(con, id);
        } finally {
            close(con);
        }
    }

    @Override
    public long createUser(HttpServletRequest req) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            setCommit(con, false);

            long generatedId = signUp(req);
            Connection finalCon = con;
            dao.get(con, generatedId).ifPresent(user -> {
                user.setUser_type(UserType.valueOf(req.getParameter(USER_TYPE_ATTR)));
                dao.update(finalCon, user);
            });

            commit(con);
            return generatedId;
        } catch (DAOException e) {
            log.error("Can't create new user, cause: " + e.getMessage(), e);
            throw new ServiceException("Can't create new user!", e);
        } finally {
            close(con);
        }
    }

    @Override
    public int getUserCount() {
        Connection con = null;
        try {
            con = getConnection();
            return CountRecordsUtil.getRecordsCount(con, USER_ID, USER_TABLE);
        } finally {
            close(con);
        }
    }
}
