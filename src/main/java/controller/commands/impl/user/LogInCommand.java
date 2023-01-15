package controller.commands.impl.user;

import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;
import services.impl.UserServiceImpl;

import static constants.PageConstants.*;
import static constants.AttributeConstants.*;
import static exceptions.ErrorType.*;

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
            req.getSession().removeAttribute(ERROR);
            return CABINET_PAGE;
        } catch (ServiceException e) {
            log.error("Authorization failed, cause: " + e.getMessage());
            req.getSession().setAttribute(ERROR, e.getMessage().equals(EMAIL.getValue()) ? EMAIL : PASSWORD);
            return LOGIN_PAGE;
        }
    }
}
