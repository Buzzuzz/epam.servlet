package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.services.impl.CourseServiceImpl;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.utils.RequestBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeleteCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseServiceImpl.getInstance().deleteCourse(Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_COURSES_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(req.getParameterMap())
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
