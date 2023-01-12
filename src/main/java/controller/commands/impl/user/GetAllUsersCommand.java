package controller.commands.impl.user;

import constants.AttributeConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.UserType;
import services.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(AttributeConstants.USERS_ATTR, UserServiceImpl.getInstance().getAllUsers());
        req.setAttribute(AttributeConstants.USER_TYPES,
                Arrays
                        .stream(UserType.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()));
        return PageConstants.USERS_PAGE;
    }
}
