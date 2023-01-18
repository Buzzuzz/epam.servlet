package controller.commands.impl.course;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;
import services.impl.CourseServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

public class WithdrawStudentCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        ErrorType error = CourseServiceImpl.getInstance().withdrawStudent(
                currentUser.getU_id(),
                Long.parseLong(req.getParameter(COURSE_ID))
        );
        req.setAttribute(ERROR_ATTR, error);
        return RequestBuilder.buildCommand(
                req.getServletPath(),
                CommandNameConstants.GET_ALL_COURSES_COMMAND,
                RequestBuilder.getSpecifiedParamsMap(req.getParameterMap()));
    }
}
