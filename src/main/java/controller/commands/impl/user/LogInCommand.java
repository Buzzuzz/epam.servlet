package controller.commands.impl.user;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import static constants.PageConstants.*;
import static constants.AttributeConstants.*;

public class LogInCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        if (req.getSession().getAttribute(LOGGED_USER_ATTR) != null) {
            return CABINET_PAGE;
        }
        return LOGIN_PAGE;
    }
}
