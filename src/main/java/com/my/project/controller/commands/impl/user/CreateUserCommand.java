package com.my.project.controller.commands.impl.user;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.UserType;
import com.my.project.services.UserService;
import com.my.project.services.dto.UserDTO;
import com.my.project.services.impl.UserServiceImpl;
import com.my.project.utils.RequestBuilder;

import static com.my.project.constants.AttributeConstants.*;

@Log4j2
public class CreateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();
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
