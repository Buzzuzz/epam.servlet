package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.model.entities.UserCourse;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserCourseDTO;
import com.servlet.ejournal.utils.RequestBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class UpdateCourseMarks implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(AttributeConstants.APPLICATION_CONTEXT);
            UserService userService = context.getUserService();
            CourseService courseService = context.getCourseService();
            long courseId = Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID));
            List<UserCourseDTO> userCourseList = userService.getEnrolledStudents(courseId);
            ValidationError error = ValidationError.NONE;

            for (UserCourseDTO ucDto : userCourseList) {
                double newMark = Double.parseDouble(req.getParameter(String.valueOf(ucDto.getUserId())));
                error = courseService.updateStudentMark(new UserCourse(
                        ucDto.getUserCourseId(),
                        ucDto.getUserId(),
                        ucDto.getCourseId(),
                        ucDto.getRegistrationDate(),
                        ucDto.getFinalMark()
                ), newMark);

                if (error != ValidationError.NONE) break;
            }

            req.setAttribute(AttributeConstants.COURSE_END_DATE, req.getParameter(AttributeConstants.COURSE_END_DATE));
            req.setAttribute(AttributeConstants.COURSE_ID, req.getParameter(AttributeConstants.COURSE_ID));
            req.setAttribute(AttributeConstants.USERS_ATTR, userCourseList);
            req.getSession().setAttribute(AttributeConstants.ERROR_ATTR, error);

            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.COURSE_MARKS,
                    req.getParameterMap());
        } catch (NumberFormatException e) {
            log.error("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
            throw new CommandException("No such course with id: " + req.getParameter(AttributeConstants.COURSE_ID), e);
        }
    }
}
