package controller.commands.impl.course;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.CourseServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;
import static constants.CommandNameConstants.*;

public class DeleteCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseServiceImpl.getInstance().deleteCourse(Long.parseLong(req.getParameter(COURSE_ID)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    GET_ALL_COURSES_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(req.getParameterMap())
            );
        } catch (Exception e) {
            throw new CommandException(e.getMessage(), e);
        }
    }
}
