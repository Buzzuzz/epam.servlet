package controller.commands.impl;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.CourseServiceImpl;
import static constants.AttributeConstants.*;

public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(COURSES_ATTR, CourseServiceImpl.getInstance().getAllCourses(req));
        return PageConstants.COURSES_PAGE;
    }
}
