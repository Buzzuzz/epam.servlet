package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

@Log4j2
public class CreateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
            UserDTO userDTO = new UserDTO(
                    0,
                    req.getParameter(AttributeConstants.EMAIL_ATTR),
                    req.getParameter(AttributeConstants.FIRST_NAME),
                    req.getParameter(AttributeConstants.LAST_NAME),
                    req.getParameter(AttributeConstants.PHONE_NUMBER),
                    UserType.STUDENT.name(),
                    Boolean.FALSE.toString(),
                    Boolean.FALSE.toString()
            );

            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, service.createUser(
                    userDTO,
                    req.getParameter(AttributeConstants.PASSWORD_ATTR),
                    req.getParameter(AttributeConstants.PASSWORD_REPEAT_ATTR),
                    req.getParameter(AttributeConstants.USER_TYPE_ATTR)));

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
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
