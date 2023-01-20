package com.my.project.controller.commands.impl.user;

import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.impl.UserServiceImpl;
import com.my.project.utils.RequestBuilder;

import static com.my.project.constants.AttributeConstants.*;

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
                            ERROR_ATTR,
                            USER_TYPE_DB));
        } catch (ServiceException e) {
            log.error("Can't change user(id) status: " + req.getParameter(USER_ID), e);
            throw new CommandException("Can't change user(id) status: " + req.getParameter(USER_ID), e);
        }
    }
}
