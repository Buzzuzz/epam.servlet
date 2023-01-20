package com.my.project.controller.commands.impl.course;

import com.my.project.constants.PageConstants;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.utils.SqlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.User;
import com.my.project.services.CourseService;
import com.my.project.services.UserService;
import com.my.project.services.impl.UserServiceImpl;

import java.util.Map;

import static com.my.project.constants.AttributeConstants.*;

@Log4j2
public class GetCourseMarksCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            long courseId = Long.parseLong(req.getParameter(COURSE_ID));
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            CourseService courseService = CourseServiceImpl.getInstance();
            UserService userService = UserServiceImpl.getInstance();

            Map<String, String[]> filters = SqlUtil.getFilters(req, COURSE_ID);
//            filters.put(String.format("%s.%s", COURSE_TABLE, COURSE_ID), new String[]{req.getParameter(COURSE_ID)});
            filters.put(String.format(FULL_COLUMN_NAME, TEACHER_COURSE_TABLE, TEACHER_ID), new String[]{String.valueOf(currentUser.getU_id())});

            req.setAttribute(COURSE_ID, req.getParameter(COURSE_ID));
            req.setAttribute(USERS_ATTR, userService.getEnrolledStudents(courseId));
            courseService.getCourse(courseId)
                    .flatMap(courseService::getCourseDTO)
                    .ifPresent(dto -> req.setAttribute(COURSE_END_DATE, dto.getEndDate()));

            return PageConstants.MARKS_PAGE;
        } catch (NumberFormatException e) {
            log.error("No such course with id: " + req.getParameter(COURSE_ID), e);
            throw new CommandException("No such course with id: " + req.getParameter(COURSE_ID), e);
        }
    }
}
