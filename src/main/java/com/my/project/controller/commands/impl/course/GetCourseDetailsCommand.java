package com.my.project.controller.commands.impl.course;

import com.my.project.constants.AttributeConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.entities.Course;
import com.my.project.model.entities.User;
import com.my.project.services.CourseService;
import com.my.project.services.impl.CourseServiceImpl;

import static com.my.project.constants.PageConstants.COURSE_DETAILS_PAGE;

@Log4j2
public class GetCourseDetailsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            long courseId = Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID));
            User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            Course course = service.getCourse(courseId).get();
            req.setAttribute(AttributeConstants.COURSE_DTO, service.getCourseDTO(course).get());
            req.setAttribute(AttributeConstants.FINAL_MARK, service.getStudentMark(courseId, currentUser.getU_id()));
            return COURSE_DETAILS_PAGE;
        } catch (Exception e) {
            log.error("Course details can't be acquired", e);
            throw new CommandException("Something went horribly wrong!", e);
        }
    }
}
