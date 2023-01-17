package controller.commands.impl.course;

import constants.AttributeConstants;
import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;
import services.UserService;
import services.impl.CourseServiceImpl;
import services.impl.UserServiceImpl;
import utils.RequestBuilder;

import java.util.HashMap;

import static constants.AttributeConstants.*;
import static constants.CommandNameConstants.*;

public class EnrollUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        ErrorType error = CourseServiceImpl.getInstance().enrollUser(
                currentUser.getU_id(),
                Long.parseLong(req.getParameter(COURSE_ID)));

        req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);
        return RequestBuilder.buildCommand(req.getServletPath(), COURSE_DETAILS_COMMAND,
                RequestBuilder.getSpecifiedParamsMap(req.getParameterMap(), COURSE_ID));
    }
}
