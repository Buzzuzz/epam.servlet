package services;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;

public interface UserService {
    User logIn(String email, String password) throws ServiceException;
    void logOut(HttpServletRequest req);
    boolean signUp(HttpServletRequest req) throws ServiceException;
    boolean updateUserData(HttpServletRequest req) throws ServiceException;
}
