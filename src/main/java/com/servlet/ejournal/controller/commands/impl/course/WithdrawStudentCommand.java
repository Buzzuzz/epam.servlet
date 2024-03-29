package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.utils.RequestBuilder;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class WithdrawStudentCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        CourseService service = context.getCourseService();
        try {
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            ValidationError error = service.withdrawStudent(currentUser.getU_id(), Long.parseLong(req.getParameter(COURSE_ID)));
            req.setAttribute(ERROR_ATTR, error);
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_COURSES_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(req.getParameterMap()));
        } catch (NumberFormatException e) {
            log.error("No such course with c_id: " + req.getParameter(COURSE_ID), e);
            throw new CommandException("No such course with c_id: " + req.getParameter(COURSE_ID), e);
        }
    }
}
