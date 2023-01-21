package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

@Log4j2
public class DeleteUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().deleteUser(Long.parseLong(req.getParameter(AttributeConstants.USER_ID)));
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
        } catch (Exception e) {
            log.error("Can't delete user", e);
            throw new CommandException("Can't execute delete-user command", e);
        }
    }
}
