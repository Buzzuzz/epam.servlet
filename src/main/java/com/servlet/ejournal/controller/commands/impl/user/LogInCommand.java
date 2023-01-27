package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.User;

import static com.servlet.ejournal.constants.PageConstants.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.exceptions.ValidationError.*;

@Log4j2
public class LogInCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        UserService service = context.getUserService();
        try {
            if (req.getSession().getAttribute(LOGGED_USER_ATTR) != null) {
                return CABINET_PAGE;
            }
            User user = service.logIn(req.getParameter(EMAIL_ATTR), req.getParameter(PASSWORD_ATTR));
            req.getSession().setAttribute(LOGGED_USER_ATTR, user);
            req.getSession().setAttribute(USER_TYPE_ATTR, user.getUser_type());
            req.getSession().removeAttribute(ERROR_ATTR);
            return CABINET_PAGE;
        } catch (ServiceException e) {
            log.error("Authorization failed, cause: " + e.getMessage());
            req.getSession().setAttribute(ERROR_ATTR, e.getMessage().equals(EMAIL.getValue()) ? EMAIL : PASSWORD);
            return LOGIN_PAGE;
        }
    }
}
