package com.servlet.ejournal.services.impl;

import com.servlet.ejournal.annotations.Transaction;
import com.servlet.ejournal.annotations.handlers.TransactionHandler;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.*;
import com.servlet.ejournal.utils.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.utils.ValidationUtil.*;
import static com.servlet.ejournal.exceptions.ValidationError.*;
import static com.servlet.ejournal.services.UserService.*;

@Log4j2
@Getter
public class UserServiceImpl implements UserService {
    private static UserService instance;
    private final UserDAO userDAO;
    private final DAO<UserCourse> userCourseDAO;
    private final HikariDataSource source;

    private UserServiceImpl(ApplicationContext context) {
        this.source = context.getDataSource();
        this.userDAO = (UserDAO) context.getUserDAO();
        this.userCourseDAO = context.getUserCourseDAO();
    }

    public static synchronized UserService getInstance(ApplicationContext context) {
        if (instance == null) {
            instance = new UserServiceImpl(context);
        }
        return instance;
    }

    @Override
    public Optional<User> getUser(long id) {
        try (Connection con = source.getConnection()) {
            return userDAO.get(con, id);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public int getUserCount(Map<String, String[]> filters) {
        try (Connection con = source.getConnection()) {
            return SqlUtil.getRecordsCount(con, USER_ID, USER_TABLE, filters);
        } catch (SQLException | DAOException e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public List<UserDTO> getAllUsers(int limit, int offset, String sorting, Map<String, String[]> filters) {
        try (Connection con = source.getConnection()) {
            return userDAO
                    .getAll(con, limit, offset, sorting, filters)
                    .stream()
                    .map(UserService::getUserDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserDTO> getAllUsers(UserType type) {
        Map<String, String[]> filters = new HashMap<>();
        filters.put(USER_TYPE_DB, new String[]{type.name()});
        return getAllUsers(getUserCount(filters), 0, DEFAULT_USER_SORTING, filters);
    }

    @Override
    public List<String> getAllUserTypes() {
        return Arrays.stream(UserType.values()).map(Enum::name).collect(Collectors.toList());
    }


    @Override
    public List<UserCourseDTO> getEnrolledStudents(long courseId) {
        try {
            TransactionHandler handler = TransactionHandler.getInstance(source);
            return handler.runTransaction(this, "getEnrolledStudentsTransaction", courseId);
        } catch (TransactionException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public ValidationError createUser(UserDTO userDTO, String password, String repeatPassword, String type) throws ServiceException {
        try {
            TransactionHandler handler = TransactionHandler.getInstance(source);
            return handler.runTransaction(this, "createUserTransaction", userDTO, password, repeatPassword, type);
        } catch (TransactionException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't run transaction!", e);
        }
    }

    @Override
    public ValidationError updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException {
        User user = getUserFromDTO(userDTO, newPassword);
        try (Connection con = source.getConnection()) {
            ValidationError passwordCheck = validatePassword(newPassword);
            ValidationError repeatPasswordCheck = validateRepeatPassword(newPassword, repeatPassword);
            ValidationError phoneNumberCheck = validatePhoneNumber(user.getPhone());

            if (passwordCheck == repeatPasswordCheck && passwordCheck == phoneNumberCheck) {
                user.setPassword(user.getPassword().equals("") ? oldPassword :
                        PasswordHashUtil.encode(user.getPassword()));
                userDAO.update(con, user);
                log.debug("User " + user.getEmail() + " updated successful");
                return NONE;
            }

            if (passwordCheck != NONE) return passwordCheck;
            if (repeatPasswordCheck != NONE) return repeatPasswordCheck;
            return phoneNumberCheck;
        } catch (DAOException | SQLException e) {
            log.error("Can't update user " + user.getEmail(), e);
            throw new ServiceException("Can't update user " + user.getEmail(), e);
        }
    }

    @Override
    public long deleteUser(long id) throws ServiceException {
        try (Connection con = source.getConnection()) {
            return userDAO.delete(con, id);
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public long changeUserLockStatus(long id, boolean status) throws ServiceException {
        long[] generated = new long[1];
        try (Connection con = source.getConnection()) {
            userDAO.get(con, id).ifPresent(user -> {
                user.set_blocked(status);
                generated[0] = userDAO.update(con, user);
            });
        } catch (Exception e) {
            log.error("Can't change user lock status: " + id, e);
            throw new ServiceException("Can't change user lock status: " + id, e);
        }
        return generated[0];
    }

    @Override
    public ValidationError signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        User user = getUserFromDTO(userDTO, password);
        try (Connection con = source.getConnection()) {
            ValidationError newUserValidation = isNewUserValid(userDAO, con, user, repeatPassword);
            if (newUserValidation == NONE) {
                user.setPassword(PasswordHashUtil.encode(user.getPassword()));
                userDAO.save(con, user);
                log.info(String.format("User %s registered successfully!", user.getEmail()));
                return NONE;
            }
            return newUserValidation;
        } catch (DAOException | SQLException | UtilException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't register new user", e);
        }
    }

    @Override
    public User logIn(String email, String password) throws ServiceException {
        try (Connection con = source.getConnection()) {
            Optional<User> daoResult = userDAO.getByEmail(con, email);

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
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't log in user " + email, e);
        }
    }

    // Transactions logic

    @Transaction
    private ValidationError createUserTransaction(Object con, Object userDTO, Object password, Object repeatPassword, Object type) throws ServiceException {
        User user = getUserFromDTO((UserDTO) userDTO, (String) password);
        try {
            ValidationError error = signUp((UserDTO) userDTO, (String) password, (String) repeatPassword);
            if (error.equals(NONE)) {
                userDAO.getByEmail((Connection) con, user.getEmail()).ifPresent(u -> {
                    u.setUser_type(UserType.valueOf((String) type));
                    userDAO.update((Connection) con, u);
                });
            }
            return error;
        } catch (DAOException | IllegalArgumentException e) {
            log.error(String.format("Can't create new user, cause: %s", e.getMessage()), e);
            throw new ServiceException("Can't create new user!", e);
        }
    }

    @Transaction
    private List<UserCourseDTO> getEnrolledStudentsTransaction(Connection con, long courseId) {
        List<UserCourseDTO> users = new ArrayList<>();
        Map<String, String[]> filters = new HashMap<>();
        filters.put(COURSE_ID, new String[]{String.valueOf(courseId)});
        filters.put(QUERY, new String[]{String.format("%s > %s", FINAL_MARK, -1)});
        try {
            List<UserCourse> userCourseList = (List<UserCourse>) userCourseDAO.getAll(
                    con,
                    SqlUtil.getRecordsCount(con, USER_ID, USER_COURSE_TABLE, filters),
                    0,
                    USER_ID,
                    filters);

            for (UserCourse uc : userCourseList) {
                getUser(uc.getU_id())
                        .ifPresent(value -> users.add(new UserCourseDTO(
                                getUserDTO(value),
                                uc.getU_c_id(),
                                uc.getU_id(),
                                uc.getC_id(),
                                uc.getRegistration_date(),
                                uc.getFinal_mark())));
            }

        } catch (DAOException e) {
            log.error(e.getMessage(), e);
        }
        return users;
    }
}
