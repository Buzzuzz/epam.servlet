package controller.commands.impl.user;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.UserType;
import services.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;

@Log4j2
public class GetAllUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(USERS_ATTR, UserServiceImpl.getInstance().getAllUsers(req));
        req.setAttribute(USER_TYPES,
                Arrays
                        .stream(UserType.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()));

        req.setAttribute(RECORDS, req.getAttribute(RECORDS));
        req.setAttribute(SORTING_TYPE, req.getAttribute(SORTING_TYPE));
        req.setAttribute(DISPLAY_RECORDS_NUMBER, req.getAttribute(DISPLAY_RECORDS_NUMBER));
        req.setAttribute(CURRENT_PAGE, req.getAttribute(CURRENT_PAGE));
        return PageConstants.USERS_PAGE;
    }
}
