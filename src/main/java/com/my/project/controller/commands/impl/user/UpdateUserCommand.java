package com.my.project.controller.commands.impl.user;

import com.my.project.constants.PageConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ValidationError;
import com.my.project.model.entities.User;
import com.my.project.services.UserService;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.services.impl.UserServiceImpl;

import static com.my.project.constants.AttributeConstants.*;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);

            ValidationError error = service.updateUserData(
                    service.getUserDTO(user),
                    user.getPassword(),
                    req.getParameter(PASSWORD_ATTR),
                    req.getParameter(PASSWORD_REPEAT_ATTR));

            req.getSession().setAttribute(ERROR_ATTR, error);

            if (error.equals(ValidationError.NONE)) {
                service.getUser(user.getU_id()).ifPresent(u -> req.getSession().setAttribute(LOGGED_USER_ATTR, u));
            }
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
