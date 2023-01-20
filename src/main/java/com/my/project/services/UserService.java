package com.my.project.services;

import com.my.project.exceptions.ValidationError;
import com.my.project.model.entities.UserType;
import com.my.project.services.dto.UserCourseDTO;
import com.my.project.services.dto.UserDTO;
import com.my.project.exceptions.ServiceException;
import com.my.project.model.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User logIn(String email, String password) throws ServiceException;

    ValidationError signUp(UserDTO userDTO, String password, String repeatPassword) throws ServiceException;

    ValidationError updateUserData(UserDTO userDTO, String oldPassword, String newPassword, String repeatPassword) throws ServiceException;

    UserDTO getUserDTO(User user);

    User getUserFromDTO(UserDTO user, String password) throws ServiceException;

    List<UserDTO> getAllUsers(int limit, int offset, String sorting, Map<String, String[]> filters);

    List<UserDTO> getAllUsers(UserType type);

    List<UserCourseDTO> getEnrolledStudents(long courseId);

    long deleteUser(long id) throws ServiceException;

    long changeUserLockStatus(long id, boolean status) throws ServiceException;

    Optional<User> getUser(long id);

    ValidationError createUser(UserDTO userDTO, String password, String repeatPassword) throws ServiceException;

    int getUserCount(Map<String, String[]> filters);
}
