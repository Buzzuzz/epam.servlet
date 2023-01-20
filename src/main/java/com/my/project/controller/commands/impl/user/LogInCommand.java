package com.my.project.controller.commands.impl.user;

import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.User;
import com.my.project.services.impl.UserServiceImpl;

import static com.my.project.constants.PageConstants.*;
import static com.my.project.constants.AttributeConstants.*;
import static com.my.project.exceptions.ValidationError.*;

@Log4j2
public class LogInCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            if (req.getSession().getAttribute(LOGGED_USER_ATTR) != null) {
                return CABINET_PAGE;
            }
            User user = UserServiceImpl.getInstance().logIn(
                    req.getParameter(EMAIL_ATTR),
                    req.getParameter(PASSWORD_ATTR));
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
