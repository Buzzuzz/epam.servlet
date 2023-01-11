package controller.commands.impl;

import constants.AttributeConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.UserServiceImpl;

public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(AttributeConstants.USERS_ATTR, UserServiceImpl.getInstance().getAllUsers());
        return PageConstants.USERS_PAGE;
    }
}
