package controller.commands.impl.user;

import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import exceptions.ServiceException;
import model.entities.UserType;
import services.dto.UserDTO;
import services.impl.UserServiceImpl;

import static constants.PageConstants.*;
import static constants.AttributeConstants.*;
import static exceptions.ErrorType.NONE;

@Log4j2
public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ErrorType error = UserServiceImpl.getInstance().signUp(
                    new UserDTO(
                            0,
                            req.getParameter(EMAIL_ATTR),
                            req.getParameter(FIRST_NAME),
                            req.getParameter(LAST_NAME),
                            req.getParameter(PHONE_NUMBER),
                            UserType.STUDENT.name(),
                            Boolean.FALSE.toString(),
                            Boolean.FALSE.toString()
                    ),
                    req.getParameter(PASSWORD_ATTR),
                    req.getParameter(PASSWORD_REPEAT_ATTR)
            );
            if (error.equals(NONE)) {
                return LOGIN_PAGE;
            }
            req.getSession().setAttribute(ERROR_ATTR, error);
            return SIGN_UP_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }
}
