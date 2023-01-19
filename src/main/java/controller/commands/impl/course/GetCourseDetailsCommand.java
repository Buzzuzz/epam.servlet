package controller.commands.impl.course;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.Course;
import model.entities.User;
import services.CourseService;
import services.impl.CourseServiceImpl;

import static constants.AttributeConstants.*;
import static constants.PageConstants.COURSE_DETAILS_PAGE;

@Log4j2
public class GetCourseDetailsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            long courseId = Long.parseLong(req.getParameter(COURSE_ID));
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            Course course = service.getCourse(courseId).get();
            req.setAttribute(COURSE_DTO, service.getCourseDTO(course).get());
            req.setAttribute(FINAL_MARK, service.getStudentMark(courseId, currentUser.getU_id()));
            return COURSE_DETAILS_PAGE;
        } catch (Exception e) {
            log.error("Course details can't be acquired", e);
            throw new CommandException("Something went horribly wrong!", e);
        }
    }
}
