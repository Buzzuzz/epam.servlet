package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.services.impl.UserServiceImpl;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            User user = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);

            ValidationError error = service.updateUserData(
                    service.getUserDTO(user),
                    user.getPassword(),
                    req.getParameter(AttributeConstants.PASSWORD_ATTR),
                    req.getParameter(AttributeConstants.PASSWORD_REPEAT_ATTR));

            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);

            if (error.equals(ValidationError.NONE)) {
                service.getUser(user.getU_id()).ifPresent(u -> req.getSession().setAttribute(AttributeConstants.LOGGED_USER_ATTR, u));
            }
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
