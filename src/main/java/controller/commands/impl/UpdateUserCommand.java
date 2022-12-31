package controller.commands.impl;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.UserServiceImpl;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().updateUserData(req);
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
