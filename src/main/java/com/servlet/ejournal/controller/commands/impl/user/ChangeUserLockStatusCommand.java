package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

@Log4j2
public class ChangeUserLockStatusCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().changeUserLockStatus(
                    Long.parseLong(req.getParameter(AttributeConstants.USER_ID)),
                    !Boolean.parseBoolean(req.getParameter(AttributeConstants.USER_STATUS)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_USERS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            AttributeConstants.SORTING_TYPE,
                            AttributeConstants.DISPLAY_RECORDS_NUMBER,
                            AttributeConstants.CURRENT_PAGE,
                            AttributeConstants.ERROR_ATTR,
                            AttributeConstants.USER_TYPE_DB));
        } catch (ServiceException e) {
            log.error("Can't change user(id) status: " + req.getParameter(AttributeConstants.USER_ID), e);
            throw new CommandException("Can't change user(id) status: " + req.getParameter(AttributeConstants.USER_ID), e);
        }
    }
}
