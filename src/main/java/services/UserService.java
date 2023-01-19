package services;

import exceptions.ErrorType;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;
import model.entities.UserType;
import services.dto.UserCourseDTO;
import services.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User logIn(String email, String password) throws ServiceException;

    ErrorType signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException;

    ErrorType updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException;

    UserDTO getUserDTO(User user);

    User getUserFromDTO(UserDTO user, String password) throws ServiceException;

    List<UserDTO> getAllUsers(int limit, int offset, String sorting, Map<String, String[]> filters);

    List<UserDTO> getAllUsers(UserType type);

    List<UserCourseDTO> getEnrolledStudents(long courseId);

    long deleteUser(long id) throws ServiceException;

    long changeUserLockStatus(long id, boolean status) throws ServiceException;

    Optional<User> getUser(long id);

    ErrorType createUser(UserDTO userDTO, String password, String repeatPassword) throws ServiceException;

    int getUserCount(Map<String, String[]> filters);
}
