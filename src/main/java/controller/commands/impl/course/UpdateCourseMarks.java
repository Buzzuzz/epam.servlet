package controller.commands.impl.course;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.UserCourse;
import services.CourseService;
import services.UserService;
import services.dto.UserCourseDTO;
import services.impl.CourseServiceImpl;
import services.impl.UserServiceImpl;
import utils.RequestBuilder;

import java.util.List;

import static constants.AttributeConstants.*;

public class UpdateCourseMarks implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        UserService userService = UserServiceImpl.getInstance();
        CourseService courseService = CourseServiceImpl.getInstance();
        long courseId = Long.parseLong(req.getParameter(COURSE_ID));
        List<UserCourseDTO> userCourseList = userService.getEnrolledStudents(courseId);
        ErrorType error = ErrorType.NONE;

        for (UserCourseDTO ucDto : userCourseList) {
            double newMark = Double.parseDouble(req.getParameter(String.valueOf(ucDto.getUserId())));
            error = courseService.updateStudentMark(new UserCourse(
                    ucDto.getUserCourseId(),
                    ucDto.getUserId(),
                    ucDto.getCourseId(),
                    ucDto.getRegistrationDate(),
                    ucDto.getFinalMark()
            ), newMark);

            if (error != ErrorType.NONE) break;
        }

        req.setAttribute(COURSE_END_DATE, req.getParameter(COURSE_END_DATE));
        req.setAttribute(COURSE_ID, req.getParameter(COURSE_ID));
        req.setAttribute(USERS_ATTR, userCourseList);
        req.setAttribute(ERROR_ATTR, error);

        return RequestBuilder.buildCommand(
                req.getServletPath(),
                CommandNameConstants.COURSE_MARKS,
                req.getParameterMap());
    }
}
