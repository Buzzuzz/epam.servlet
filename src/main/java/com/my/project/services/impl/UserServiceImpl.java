package com.my.project.services.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.exceptions.DAOException;
import com.my.project.exceptions.ValidationError;
import com.my.project.exceptions.ServiceException;
import com.my.project.model.dao.DataSource;
import com.my.project.model.dao.impl.UserCourseDAO;
import com.my.project.model.dao.impl.UserDAO;
import com.my.project.model.entities.User;
import com.my.project.model.entities.UserCourse;
import com.my.project.model.entities.UserType;
import com.my.project.services.UserService;
import com.my.project.services.dto.UserCourseDTO;
import com.my.project.services.dto.UserDTO;
import com.my.project.utils.PaginationUtil;
import com.my.project.utils.PasswordHashUtil;
import com.my.project.utils.ValidationUtil;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

import static com.my.project.model.dao.DataSource.*;

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
            DataSource.close(con);

            if (daoResult.isPresent()) {
                User user = daoResult.get();
                if (PasswordHashUtil.verify(user.getPassword(), password)) {
                    log.info("User " + user.getEmail() + " logged in successful!");
                    return user;
                } else {
                    log.debug("Password doesn't match! " + user.getEmail());
                    throw new ServiceException(AttributeConstants.PASSWORD_ATTR);
                }
            } else {
                log.debug("No user with email " + email);
                throw new ServiceException(AttributeConstants.EMAIL_ATTR);
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't log in user " + email);
        }
    }

    public ValidationError signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        User user = getUserFromDTO(userDTO, password);
        if (ValidationUtil.isNewUserValid(user, repeatPassword).equals(ValidationError.NONE)) {
            user.setPassword(PasswordHashUtil.encode(user.getPassword()));
            Connection con = null;
            try {
                con = getConnection();
                userDAO.save(con, user);
                log.info(String.format("User %s registered successfully!", user.getEmail()));
                return ValidationError.NONE;
            } catch (DAOException e) {
                log.error(e.getMessage(), e);
                throw new ServiceException("Can't register new user", e);
            } finally {
                close(con);
            }
        }
        return ValidationUtil.isNewUserValid(user, repeatPassword);
    }

    public ValidationError updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException {
        Connection con = null;
        User user = getUserFromDTO(userDTO, newPassword);
        try {
            con = DataSource.getConnection();
            if (ValidationUtil.validatePassword(newPassword).equals(ValidationError.NONE) &&
                    ValidationUtil.validateRepeatPassword(newPassword, repeatPassword).equals(ValidationError.NONE) &&
                    ValidationUtil.validatePhoneNumber(user.getPhone()).equals(ValidationError.NONE)) {
                user.setPassword(user.getPassword().equals("") ? oldPassword :
                        PasswordHashUtil.encode(user.getPassword()));
                userDAO.update(con, user);
                log.debug("User " + user.getEmail() + " updated successful");
                return ValidationError.NONE;
            }

            if (ValidationUtil.validatePassword(newPassword).equals(ValidationError.NONE)) {
                if (ValidationUtil.validateRepeatPassword(newPassword, repeatPassword).equals(ValidationError.NONE)) {
                    if (ValidationUtil.validatePhoneNumber(user.getPhone()).equals(ValidationError.NONE)) {
                        return ValidationError.NONE;
                    } else return ValidationError.PHONE_NUMBER;
                } else return ValidationError.PASSWORD_REPEAT;
            } else return ValidationError.PASSWORD;

        } catch (DAOException e) {
            log.error("Can't update user " + user.getEmail(), e);
            throw new ServiceException("Can't update user " + user.getEmail(), e);
        } finally {
            DataSource.close(con);
        }
    }

    @Override
    public UserDTO getUserDTO(User user) {
        Connection con = null;

        try {
            con = DataSource.getConnection();

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
            DataSource.close(con);
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
            con = DataSource.getConnection();
            return userDAO
                    .getAll(con, limit, offset, sorting, filters)
                    .stream()
                    .map(this::getUserDTO)
                    .collect(Collectors.toList());
        } finally {
            DataSource.close(con);
        }
    }

    @Override
    public List<UserDTO> getAllUsers(UserType type) {
        return getAllUsers(
                getUserCount(null), 0, AttributeConstants.DEFAULT_USER_SORTING,
                new HashMap<String, String[]>() {{
                    put(AttributeConstants.USER_TYPE_DB, new String[]{type.name()});
                }});
    }

    @Override
    public List<UserCourseDTO> getEnrolledStudents(long courseId) {
        Connection con = null;
        List<UserCourseDTO> users = new ArrayList<>();

        Map<String, String[]> filters = new HashMap<>();
        filters.put(AttributeConstants.COURSE_ID, new String[]{String.valueOf(courseId)});
        filters.put(AttributeConstants.QUERY, new String[]{String.format("%s > %s", AttributeConstants.FINAL_MARK, -1)});
        try {
            con = DataSource.getConnection();
            DataSource.setAutoCommit(con, false);

            List<UserCourse> userCourseList = (List<UserCourse>) userCourseDAO.getAll(con,
                    PaginationUtil.getRecordsCount(con, AttributeConstants.USER_ID, AttributeConstants.USER_COURSE_TABLE, filters), 0, AttributeConstants.USER_ID, filters);

            for (UserCourse uc : userCourseList) {
                Optional<User> user = getUser(uc.getU_id());
                user.ifPresent(u -> log.info(uc.getFinal_mark()));
                user.ifPresent(value -> users.add(new UserCourseDTO(
                        getUserDTO(value),
                        uc.getU_c_id(),
                        uc.getU_id(),
                        uc.getC_id(),
                        uc.getRegistration_date(),
                        uc.getFinal_mark())));
            }

            DataSource.commit(con);
        } catch (DAOException e) {
            DataSource.rollback(con);
            log.error(e.getMessage(), e);
        } finally {
            DataSource.setAutoCommit(con, true);
            DataSource.close(con);
        }
        log.info(users);
        return users;
    }

    @Override
    public long deleteUser(long id) throws ServiceException {
        Connection con = null;
        try {
            con = DataSource.getConnection();
            return userDAO.delete(con, id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            DataSource.close(con);
        }
    }

    @Override
    public long changeUserLockStatus(long id, boolean status) throws ServiceException {
        Connection con = null;
        long[] generated = new long[1];
        try {
            con = DataSource.getConnection();
            Connection finalCon = con;
            userDAO.get(con, id).ifPresent(user -> {
                user.set_blocked(status);
                generated[0] = userDAO.update(finalCon, user);
            });
        } catch (Exception e) {
            log.error("Can't lock user: " + id, e);
            throw new ServiceException("Can't lock user: " + id, e);
        } finally {
            DataSource.close(con);
        }
        return generated[0];
    }

    @Override
    public Optional<User> getUser(long id) {
        Connection con = null;
        try {
            con = DataSource.getConnection();
            return userDAO.get(con, id);
        } finally {
            DataSource.close(con);
        }
    }

    @Override
    public ValidationError createUser(UserDTO userDTO, String password, String repeatPassword, String type) throws ServiceException {
        Connection con = null;
        User user = getUserFromDTO(userDTO, password);
        try {
            con = DataSource.getConnection();
            setAutoCommit(con, false);

            ValidationError error = signUp(userDTO, password, repeatPassword);
            if (error.equals(ValidationError.NONE)) {
                Connection finalCon = con;
                userDAO.getByEmail(con, user.getEmail()).ifPresent(u -> {
                    u.setUser_type(UserType.valueOf(type));
                    userDAO.update(finalCon, u);
                });
                commit(con);
            }
            return error;
        } catch (DAOException e) {
            rollback(con);
            log.error(String.format("Can't create new user, cause: %s", e.getMessage()), e);
            throw new ServiceException("Can't create new user!", e);
        } catch (IllegalArgumentException e) {
            log.error("Wrong userType passed!", e);
            throw new ServiceException("Can't update user! Wrong userType!", e);
        } finally {
            DataSource.setAutoCommit(con, true);
            DataSource.close(con);
        }
    }

    @Override
    public int getUserCount(Map<String, String[]> filters) {
        Connection con = null;
        try {
            con = DataSource.getConnection();
            return PaginationUtil.getRecordsCount(con, AttributeConstants.USER_ID, AttributeConstants.USER_TABLE, filters);
        } finally {
            DataSource.close(con);
        }
    }
}
