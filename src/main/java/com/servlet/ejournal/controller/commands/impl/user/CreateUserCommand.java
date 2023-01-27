package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.utils.RequestBuilder;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class CreateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
            UserService service = context.getUserService();
            UserDTO userDTO = new UserDTO(
                    0,
                    req.getParameter(EMAIL_ATTR),
                    req.getParameter(FIRST_NAME),
                    req.getParameter(LAST_NAME),
                    req.getParameter(PHONE_NUMBER),
                    UserType.STUDENT.name(),
                    Boolean.FALSE.toString(),
                    Boolean.FALSE.toString()
            );

            req.getSession().setAttribute(ERROR_ATTR, service.createUser(
                    userDTO,
                    req.getParameter(PASSWORD_ATTR),
                    req.getParameter(PASSWORD_REPEAT_ATTR),
                    req.getParameter(USER_TYPE_ATTR)));

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
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
