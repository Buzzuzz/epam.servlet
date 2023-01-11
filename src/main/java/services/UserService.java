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
    boolean signUp(HttpServletRequest req) throws ServiceException;
    boolean updateUserData(HttpServletRequest req) throws ServiceException;
    UserDTO getUserDTO(User user);
    Optional<User> getUser(long id);
    List<UserDTO> getAllUsers();
}
