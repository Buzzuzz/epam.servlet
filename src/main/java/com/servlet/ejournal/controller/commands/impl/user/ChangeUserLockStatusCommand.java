package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.utils.RequestBuilder;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class ChangeUserLockStatusCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        try {
            UserService service = context.getUserService();
            service.changeUserLockStatus(
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
        } catch (Exception e) {
            log.error("Can't change user(id) status: " + req.getParameter(USER_ID), e);
            throw new CommandException("Can't change user(id) status: " + req.getParameter(USER_ID), e);
        }
    }
}
