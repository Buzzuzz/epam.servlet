package services.impl;

import exceptions.ErrorType;
import exceptions.ServiceException;
import lombok.extern.log4j.Log4j2;
import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.impl.UserCourseDAO;
import model.dao.impl.UserDAO;
import model.entities.User;
import model.entities.UserCourse;
import model.entities.UserType;
import services.UserService;
import services.dto.UserCourseDTO;
import services.dto.UserDTO;
import utils.PaginationUtil;
import utils.PasswordHashUtil;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;
import static model.dao.DataSource.*;
import static utils.ValidationUtil.*;
import static exceptions.ErrorType.*;
import static utils.PaginationUtil.*;

@Log4j2
public class UserServiceImpl implements UserService {
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserCourseDAO userCourseDAO = UserCourseDAO.getInstance();

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
            Optional<User> daoResult = userDAO.getByEmail(con, email);
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

    public ErrorType signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        User user = getUserFromDTO(userDTO, password);
        if (isNewUserValid(user, repeatPassword).equals(NONE)) {
            Connection con = null;
            try {
                con = DataSource.getConnection();
                userDAO.save(con, user);
                log.info(String.format("User %s registered successfully!", user.getEmail()));
                return NONE;
            } catch (DAOException e) {
                log.error(e.getMessage(), e);
                throw new ServiceException("Can't register new user", e);
            } finally {
                close(con);
            }
        }
        return isNewUserValid(user, repeatPassword);
    }

    public ErrorType updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException {
        Connection con = null;
        User user = getUserFromDTO(userDTO, newPassword);
        try {
            con = DataSource.getConnection();
            if (validatePassword(newPassword).equals(NONE) &&
                    validateRepeatPassword(newPassword, repeatPassword).equals(NONE) &&
                    validatePhoneNumber(user.getPhone()).equals(NONE)) {
                user.setPassword(user.getPassword().equals("") ? oldPassword :
                        PasswordHashUtil.encode(user.getPassword()));
                userDAO.update(con, user);
                log.debug("User " + user.getEmail() + " updated successful");
                return NONE;
            }

            if (validatePassword(newPassword).equals(NONE)) {
                if (validateRepeatPassword(newPassword, repeatPassword).equals(NONE)) {
                    if (validatePhoneNumber(user.getPhone()).equals(NONE)) {
                        return NONE;
                    } else return ErrorType.PHONE_NUMBER;
                } else return PASSWORD_REPEAT;
            } else return PASSWORD;

        } catch (DAOException e) {
            log.error("Can't update user " + user.getEmail(), e);
            throw new ServiceException("Can't update user " + user.getEmail(), e);
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
                    user.getPhone(),
                    user.getUser_type().name(),
                    String.valueOf(user.is_blocked()),
                    String.valueOf(user.isSend_notification())
            );
        } finally {
            close(con);
        }
    }

    @Override
    public User getUserFromDTO(UserDTO user, String password) throws ServiceException {
        try {
            return new User(
                    user.getUserId(),
                    user.getEmail(),
                    password,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber(),
                    UserType.valueOf(user.getUserType()),
                    Boolean.parseBoolean(user.getIsBlocked()),
                    Boolean.parseBoolean(user.getSendNotification())
            );
        } catch (Exception e) {
            log.error("Can't convert userDTO to user!", e);
            throw new ServiceException("Can't convert userDTO to user!", e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers(int limit, int offset, String sorting, Map<String, String[]> filters) {
        Connection con = null;
        try {
            con = getConnection();
            return userDAO
                    .getAll(con, limit, offset, sorting, filters)
                    .stream()
                    .map(this::getUserDTO)
                    .collect(Collectors.toList());
        } finally {
            close(con);
        }
    }

    @Override
    public List<UserDTO> getAllUsers(UserType type) {
        return getAllUsers(
                getUserCount(null), 0, DEFAULT_USER_SORTING,
                new HashMap<String, String[]>() {{
                    put(USER_TYPE_DB, new String[]{type.name()});
                }});
    }

    @Override
    public List<UserCourseDTO> getEnrolledStudents(long courseId) {
        Connection con = null;
        List<UserCourseDTO> users = new ArrayList<>();

        Map<String, String[]> filters = new HashMap<>();
        filters.put(COURSE_ID, new String[]{String.valueOf(courseId)});
        filters.put(QUERY, new String[]{String.format("%s > %s", FINAL_MARK, -1)});
        try {
            con = getConnection();
            setAutoCommit(con, false);

            List<UserCourse> userCourseList = (List<UserCourse>) userCourseDAO.getAll(con,
                    getRecordsCount(con, USER_ID, USER_COURSE_TABLE, filters), 0, USER_ID, filters);

            for (UserCourse uc : userCourseList) {
                Optional<User> user = getUser(uc.getU_id());
                user.ifPresent(value -> users.add(new UserCourseDTO(
                        getUserDTO(value),
                        uc.getU_c_id(),
                        uc.getU_id(),
                        uc.getC_id(),
                        uc.getRegistration_date(),
                        uc.getFinal_mark())));
            }

            commit(con);
        } catch (DAOException e) {
            rollback(con);
            log.error(e.getMessage(), e);
        } finally {
            setAutoCommit(con, true);
            close(con);
        }
        log.info(users);
        return users;
    }

    @Override
    public long deleteUser(long id) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return userDAO.delete(con, id);
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
            userDAO.get(con, id).ifPresent(user -> {
                user.set_blocked(status);
                generated[0] = userDAO.update(finalCon, user);
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
            return userDAO.get(con, id);
        } finally {
            close(con);
        }
    }

    @Override
    public ErrorType createUser(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        Connection con = null;
        User user = getUserFromDTO(userDTO, password);
        try {
            con = getConnection();
            setAutoCommit(con, false);

            ErrorType error = signUp(userDTO, password, repeatPassword);
            if (error.equals(NONE)) {
                Connection finalCon = con;
                userDAO.getByEmail(con, user.getEmail()).ifPresent(u -> userDAO.update(finalCon, user));
                commit(con);
            }
            setAutoCommit(con, true);

            return error;
        } catch (DAOException e) {
            rollback(con);
            log.error(String.format("Can't create new user, cause: %s", e.getMessage()), e);
            throw new ServiceException("Can't create new user!", e);
        } finally {
            close(con);
        }
    }

    @Override
    public int getUserCount(Map<String, String[]> filters) {
        Connection con = null;
        try {
            con = getConnection();
            return PaginationUtil.getRecordsCount(con, USER_ID, USER_TABLE, filters);
        } finally {
            close(con);
        }
    }
}
