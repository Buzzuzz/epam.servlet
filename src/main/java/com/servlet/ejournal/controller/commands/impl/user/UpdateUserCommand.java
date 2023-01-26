package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

import static com.servlet.ejournal.constants.AttributeConstants.*;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
            UserService service = context.getUserService();
            User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            UserDTO userDTO = new UserDTO(
                    user.getU_id(),
                    user.getEmail(),
                    req.getParameter(FIRST_NAME),
                    req.getParameter(LAST_NAME),
                    req.getParameter(PHONE_NUMBER),
                    user.getUser_type().name(),
                    String.valueOf(user.is_blocked()),
                    String.valueOf(user.isSend_notification())
            );

            ValidationError error = service.updateUserData(
                    userDTO,
                    user.getPassword(),
                    req.getParameter(PASSWORD_ATTR),
                    req.getParameter(PASSWORD_REPEAT_ATTR));

            req.getSession().setAttribute(ERROR_ATTR, error);

            if (error == ValidationError.NONE) {
                service.getUser(user.getU_id()).ifPresent(u -> req.getSession().setAttribute(LOGGED_USER_ATTR, u));
            }
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }
}
