package com.my.project.controller.commands.impl.course;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.utils.RequestBuilder;

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
            throw new CommandException(e.getMessage(), e);
        }
    }
}
