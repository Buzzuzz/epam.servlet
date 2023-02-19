package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.utils.SqlUtil;
import com.servlet.ejournal.constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.*;

import java.util.Map;

@Log4j2
public class GetCourseMarksCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(AttributeConstants.APPLICATION_CONTEXT);
            long courseId = Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID));
            User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            CourseService courseService = context.getCourseService();
            UserService userService = context.getUserService();

            Map<String, String[]> filters = SqlUtil.getFilters(req, AttributeConstants.COURSE_ID);
            filters.put(String.format(AttributeConstants.FULL_COLUMN_NAME, AttributeConstants.TEACHER_COURSE_TABLE, AttributeConstants.TEACHER_ID), new String[]{String.valueOf(currentUser.getU_id())});

            req.setAttribute(AttributeConstants.COURSE_ID, req.getParameter(AttributeConstants.COURSE_ID));
            req.setAttribute(AttributeConstants.USERS_ATTR, userService.getEnrolledStudents(courseId));
            courseService.getCourse(courseId)
                    .flatMap(courseService::getCourseDTO)
                    .ifPresent(dto -> req.setAttribute(AttributeConstants.COURSE_END_DATE, dto.getEndDate()));

            return PageConstants.MARKS_PAGE;
        } catch (NumberFormatException e) {
            log.error("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
            throw new CommandException("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
        }
    }
}
