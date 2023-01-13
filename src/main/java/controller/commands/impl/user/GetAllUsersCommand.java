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

import static constants.AttributeConstants.*;

public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(USERS_ATTR, UserServiceImpl.getInstance().getAllUsers(req));
        req.setAttribute(USER_TYPES,
                Arrays
                        .stream(UserType.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()));
        req.setAttribute(RECORDS, UserServiceImpl.getInstance().getUserCount());
        return PageConstants.USERS_PAGE;
    }
}
