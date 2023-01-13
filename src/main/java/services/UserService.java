package services;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;
import services.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User logIn(String email, String password) throws ServiceException;

    void logOut(HttpServletRequest req);

    long signUp(HttpServletRequest req) throws ServiceException;

    boolean updateUserData(HttpServletRequest req) throws ServiceException;

    UserDTO getUserDTO(User user);

    List<UserDTO> getAllUsers(HttpServletRequest req);

    long deleteUser(long id) throws ServiceException;

    long changeUserLockStatus(long id, boolean status) throws ServiceException;

    Optional<User> getUser(long id);

    long createUser(HttpServletRequest req) throws ServiceException;

    int getUserCount();
}
