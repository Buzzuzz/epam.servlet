package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.CourseService;

import static com.servlet.ejournal.constants.PageConstants.COURSE_DETAILS_PAGE;

@Log4j2
public class GetCourseDetailsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(AttributeConstants.APPLICATION_CONTEXT);
            CourseService service = context.getCourseService();
            long courseId = Long.parseLong(req.getParameter(AttributeConstants.COURSE_ID));
            User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            service.getCourse(courseId)
                    .flatMap(service::getCourseDTO)
                    .ifPresent(dto -> {
                        req.setAttribute(AttributeConstants.COURSE_DTO, dto);
                        req.setAttribute(AttributeConstants.FINAL_MARK, service.getStudentMark(courseId, currentUser.getU_id()));
                    });

            return COURSE_DETAILS_PAGE;
        } catch (Exception e) {
            log.error("Course details can't be acquired", e);
            throw new CommandException("Something went horribly wrong!", e);
        }
    }
}
