package com.my.project.controller.commands.impl.user;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.PageConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ValidationError;
import com.my.project.model.entities.User;
import com.my.project.services.UserService;
import com.my.project.services.dto.UserDTO;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.services.impl.UserServiceImpl;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();

            User user = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            UserDTO userDTO = new UserDTO(
                    user.getU_id(),
                    user.getEmail(),
                    req.getParameter(AttributeConstants.FIRST_NAME),
                    req.getParameter(AttributeConstants.LAST_NAME),
                    req.getParameter(AttributeConstants.PHONE_NUMBER),
                    user.getUser_type().name(),
                    String.valueOf(user.is_blocked()),
                    String.valueOf(user.isSend_notification())
            );

            ValidationError error = service.updateUserData(
                    userDTO,
                    user.getPassword(),
                    req.getParameter(AttributeConstants.PASSWORD_ATTR),
                    req.getParameter(AttributeConstants.PASSWORD_REPEAT_ATTR));

            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);

            if (error.equals(ValidationError.NONE)) {
                req.getSession().setAttribute(AttributeConstants.LOGGED_USER_ATTR, service.getUser(user.getU_id()).get());
            }
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
