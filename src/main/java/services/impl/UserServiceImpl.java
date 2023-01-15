package services.impl;

import exceptions.ErrorType;
import exceptions.ServiceException;
import lombok.extern.log4j.Log4j2;
import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;
import model.entities.UserType;
import services.UserService;
import services.dto.UserDTO;
import utils.PaginationUtil;
import utils.PasswordHashUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;
import static model.dao.DataSource.*;
import static utils.ValidationUtil.*;
import static exceptions.ErrorType.*;

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

    public ErrorType signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        User user = getUserFromDTO(userDTO, password);
        if (isNewUserValid(user, repeatPassword).equals(NONE)) {
            Connection con = null;
            try {
                con = DataSource.getConnection();
                dao.save(con, user);
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
                dao.update(con, user);
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

    // TODO implement filtration
    @Override
    public List<UserDTO> getAllUsers(int limit, int[] pages, int currentPage, int offset, String sorting) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return dao
                    .getAll(con, limit, offset, sorting, new HashMap<>())
                    .stream()
                    .map(this::getUserDTO)
                    .collect(Collectors.toList());
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
    public ErrorType createUser(UserDTO userDTO, String password, String repeatPassword) throws ServiceException {
        Connection con = null;
        User user = getUserFromDTO(userDTO, password);
        try {
            con = getConnection();
            setAutoCommit(con, false);

            ErrorType error = signUp(userDTO, password, repeatPassword);
            if (error.equals(NONE)) {
                Connection finalCon = con;
                dao.getByEmail(con, user.getEmail()).ifPresent(u -> dao.update(finalCon, user));
                commit(con);
            }
            setAutoCommit(con, true);

            return error;
        } catch (DAOException e) {
            log.error(String.format("Can't create new user, cause: %s", e.getMessage()), e);
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
            return PaginationUtil.getRecordsCount(con, USER_ID, USER_TABLE);
        } finally {
            close(con);
        }
    }
}
