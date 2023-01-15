package controller.commands.impl.course;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.Course;
import services.CourseService;
import services.impl.CourseServiceImpl;

import static constants.AttributeConstants.COURSE_DTO;
import static constants.AttributeConstants.COURSE_ID;
import static constants.PageConstants.COURSE_DETAILS_PAGE;

@Log4j2
public class CourseDetailsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            Course course = service.getCourse(Long.parseLong(req.getParameter(COURSE_ID))).get();
            req.setAttribute(COURSE_DTO, service.getCourseDTO(course).get());
            return COURSE_DETAILS_PAGE;
        } catch (Exception e) {
            log.error("Course details can't be acquired", e);
            throw new CommandException("Something went horribly wrong!", e);
        }
    }
}
