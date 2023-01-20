package com.my.project.controller.commands.impl.user;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.PageConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ValidationError;
import com.my.project.exceptions.ServiceException;
import com.my.project.model.entities.UserType;
import com.my.project.services.dto.UserDTO;
import com.my.project.services.impl.UserServiceImpl;
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
