package controller.commands.impl;

import controller.commands.Command;
import controller.commands.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import static constants.PagesConstants.*;
import static constants.AttributesConstants.*;

public class LogInCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        if (req.getSession().getAttribute(LOGGED_USER_ATTR) != null) {
            return CABINET_PAGE;
        }
        return LOGIN_PAGE;
    }
}
