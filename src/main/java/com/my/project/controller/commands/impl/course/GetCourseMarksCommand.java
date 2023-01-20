package com.my.project.controller.commands.impl.course;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.PageConstants;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.User;
import com.my.project.services.CourseService;
import com.my.project.services.UserService;
import com.my.project.services.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class GetCourseMarksCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        long courseId = Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID));
        User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
        CourseService courseService = CourseServiceImpl.getInstance();
        UserService userService = UserServiceImpl.getInstance();

        Map<String, String[]> filters = new HashMap<>();
        filters.put(String.format("%s.%s", AttributeConstants.COURSE_TABLE, AttributeConstants.COURSE_ID), new String[]{req.getParameter(AttributeConstants.COURSE_ID)});
        filters.put(String.format("%s.%s", AttributeConstants.TEACHER_COURSE_TABLE, AttributeConstants.TEACHER_ID), new String[]{String.valueOf(currentUser.getU_id())});

        req.setAttribute(AttributeConstants.COURSE_ID, req.getParameter(AttributeConstants.COURSE_ID));
        req.setAttribute(AttributeConstants.COURSE_END_DATE, courseService.getCourseDTO(courseService.getCourse(courseId).get()).get().getEndDate());
        req.setAttribute(AttributeConstants.USERS_ATTR, userService.getEnrolledStudents(courseId));

        return PageConstants.MARKS_PAGE;
    }
}
