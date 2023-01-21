package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.services.impl.CourseServiceImpl;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.utils.RequestBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EnrollStudentCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            ValidationError error = CourseServiceImpl.getInstance().enrollStudent(
                    currentUser.getU_id(),
                    Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID)));

            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);
            return RequestBuilder.buildCommand(req.getServletPath(), CommandNameConstants.COURSE_DETAILS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(req.getParameterMap(), AttributeConstants.COURSE_ID));
        } catch (NumberFormatException e) {
            log.error("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
            throw new CommandException("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
        }
    }
}
