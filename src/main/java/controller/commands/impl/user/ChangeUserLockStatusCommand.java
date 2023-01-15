package controller.commands.impl.user;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.impl.UserServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

@Log4j2
public class ChangeUserLockStatusCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().changeUserLockStatus(
                    Long.parseLong(req.getParameter(USER_ID)),
                    !Boolean.parseBoolean(req.getParameter(USER_STATUS)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_USERS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            SORTING_TYPE,
                            DISPLAY_RECORDS_NUMBER,
                            CURRENT_PAGE,
                            ERROR));
        } catch (ServiceException e) {
            log.error("Can't change user(id) status: " + req.getParameter(USER_ID), e);
            throw new CommandException("Can't change user(id) status: " + req.getParameter(USER_ID), e);
        }
    }
}
