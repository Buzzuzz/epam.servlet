package controller.commands.impl.user;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.UserType;
import services.UserService;
import services.dto.UserDTO;
import services.impl.UserServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

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
                    req.getParameter(PASSWORD_REPEAT_ATTR)));

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
