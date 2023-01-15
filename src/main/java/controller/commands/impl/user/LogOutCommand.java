package controller.commands.impl.user;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.impl.UserServiceImpl;

@Log4j2
public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.getSession().invalidate();
        return PageConstants.LOGIN_PAGE;
    }
}
