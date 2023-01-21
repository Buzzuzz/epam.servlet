package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ValidationError error = UserServiceImpl.getInstance().signUp(
                    new UserDTO(
                            0,
                            req.getParameter(AttributeConstants.EMAIL_ATTR),
                            req.getParameter(AttributeConstants.FIRST_NAME),
                            req.getParameter(AttributeConstants.LAST_NAME),
                            req.getParameter(AttributeConstants.PHONE_NUMBER),
                            UserType.STUDENT.name(),
                            Boolean.FALSE.toString(),
                            Boolean.FALSE.toString()
                    ),
                    req.getParameter(AttributeConstants.PASSWORD_ATTR),
                    req.getParameter(AttributeConstants.PASSWORD_REPEAT_ATTR)
            );
            if (error.equals(ValidationError.NONE)) {
                return PageConstants.LOGIN_PAGE;
            }
            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);
            return PageConstants.SIGN_UP_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }
}
