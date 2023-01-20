package com.my.project.controller.commands.impl.course;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.exceptions.ValidationError;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.model.entities.UserCourse;
import com.my.project.services.CourseService;
import com.my.project.services.UserService;
import com.my.project.services.dto.UserCourseDTO;
import com.my.project.services.impl.UserServiceImpl;
import com.my.project.utils.RequestBuilder;

import java.util.List;

public class UpdateCourseMarks implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        UserService userService = UserServiceImpl.getInstance();
        CourseService courseService = CourseServiceImpl.getInstance();
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
        req.setAttribute(AttributeConstants.ERROR_ATTR, error);

        return RequestBuilder.buildCommand(
                req.getServletPath(),
                CommandNameConstants.COURSE_MARKS,
                req.getParameterMap());
    }
}
