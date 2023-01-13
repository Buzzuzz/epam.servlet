package controller.commands.impl.user;

import constants.AttributeConstants;
import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.impl.UserServiceImpl;
import utils.RequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static constants.AttributeConstants.*;

@Log4j2
public class DeleteUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().deleteUser(Long.parseLong(req.getParameter(USER_ID)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_USERS_COMMAND,
                    RequestBuilder.getParamsMap(
                            req,
                            SORTING_TYPE,
                            DISPLAY_RECORDS_NUMBER,
                            CURRENT_PAGE));
        } catch (Exception e) {
            log.error("Can't delete user", e);
            throw new CommandException("Can't execute delete-user command", e);
        }
    }
}
