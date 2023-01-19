package controller.commands.impl.user;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;
import services.CourseService;
import services.UserService;
import services.impl.CourseServiceImpl;
import services.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static constants.AttributeConstants.*;

@Log4j2
public class CourseMarksCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        long courseId = Long.parseLong(req.getParameter(COURSE_ID));
        User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        CourseService courseService = CourseServiceImpl.getInstance();
        UserService userService = UserServiceImpl.getInstance();

        Map<String, String[]> filters = new HashMap<>();
        filters.put(String.format("%s.%s", COURSE_TABLE, COURSE_ID), new String[]{req.getParameter(COURSE_ID)});
        filters.put(String.format("%s.%s", TEACHER_COURSE_TABLE, TEACHER_ID), new String[]{String.valueOf(currentUser.getU_id())});

        req.setAttribute(COURSE_ID, req.getParameter(COURSE_ID));
        req.setAttribute(COURSE_END_DATE, courseService.getCourseDTO(courseService.getCourse(courseId).get()).get().getEndDate());

        if (req.getParameter(USERS_ATTR) == null) {
            req.setAttribute(USERS_ATTR, userService.getEnrolledStudents(courseId));
        } else {
            req.setAttribute(USERS_ATTR, req.getParameter(USERS_ATTR));
        }
        return PageConstants.MARKS_PAGE;
    }
}
