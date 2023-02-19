package com.servlet.ejournal.services;

import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.dto.UserCourseDTO;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.model.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> getUser(long id);

    int getUserCount(Map<String, String[]> filters);

    List<UserDTO> getAllUsers(int limit, int offset, String sorting, Map<String, String[]> filters);

    List<UserDTO> getAllUsers(UserType type);

    List<String> getAllUserTypes();

    List<UserCourseDTO> getEnrolledStudents(long courseId);

    ValidationError createUser(UserDTO userDTO, String password, String repeatPassword, String type) throws ServiceException;

    ValidationError updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException;

    long deleteUser(long id) throws ServiceException;

    long changeUserLockStatus(long id, boolean status) throws ServiceException;

    ValidationError signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException;

    User logIn(String email, String password) throws ServiceException;

    static UserDTO getUserDTO(User user) {
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
    }

    static User getUserFromDTO(UserDTO user, String password) throws ServiceException {
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
            throw new ServiceException("Can't convert userDTO to user!", e);
        }
    }
}
