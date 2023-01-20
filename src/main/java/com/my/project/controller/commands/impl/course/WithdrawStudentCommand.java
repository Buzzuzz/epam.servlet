package com.my.project.controller.commands.impl.course;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.exceptions.ValidationError;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.model.entities.User;
import com.my.project.utils.RequestBuilder;

public class WithdrawStudentCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
        ValidationError error = CourseServiceImpl.getInstance().withdrawStudent(
                currentUser.getU_id(),
                Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID))
        );
        req.setAttribute(AttributeConstants.ERROR_ATTR, error);
        return RequestBuilder.buildCommand(
                req.getServletPath(),
                CommandNameConstants.GET_ALL_COURSES_COMMAND,
                RequestBuilder.getSpecifiedParamsMap(req.getParameterMap()));
    }
}
