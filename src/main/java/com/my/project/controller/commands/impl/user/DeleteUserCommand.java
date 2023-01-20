package com.my.project.controller.commands.impl.user;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.impl.UserServiceImpl;
import com.my.project.utils.RequestBuilder;

import static com.my.project.constants.AttributeConstants.*;

@Log4j2
public class DeleteUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().deleteUser(Long.parseLong(req.getParameter(USER_ID)));
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
            log.error("Can't delete user", e);
            throw new CommandException("Can't execute delete-user command", e);
        }
    }
}