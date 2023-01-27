package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
            UserService service = context.getUserService();
            ValidationError error = service.signUp(
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
            if (error == ValidationError.NONE) {
                return PageConstants.LOGIN_PAGE;
            }
            req.getSession().setAttribute(ERROR_ATTR, error);
            return PageConstants.SIGN_UP_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }
}
