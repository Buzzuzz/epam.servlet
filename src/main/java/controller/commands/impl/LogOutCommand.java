package controller.commands.impl;

import constants.PagesConstants;
import controller.commands.Command;
import controller.commands.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.getSession().invalidate();
        return PagesConstants.HOME_PAGE;
    }
}
