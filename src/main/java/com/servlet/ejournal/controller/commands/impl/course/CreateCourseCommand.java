package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.utils.DateFormatterUtil;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.dto.FullCourseDTO;

import java.sql.Timestamp;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class CreateCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
            CourseService service = context.getCourseService();
            Timestamp start = DateFormatterUtil.getTimestamp(req.getParameter(COURSE_START_DATE));
            Timestamp end = DateFormatterUtil.getTimestamp(req.getParameter(COURSE_END_DATE));
            FullCourseDTO courseDTO = new FullCourseDTO(
                    0,
                    Long.parseLong(req.getParameter(TOPIC_ID)),
                    Long.parseLong(req.getParameter(USER_ID)),
                    req.getParameter(COURSE_NAME),
                    req.getParameter(COURSE_DESCRIPTION),
                    start, end,
                    FullCourseUtil.getDuration(start, end),
                    0,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            ValidationError error = service.createCourse(courseDTO);
            req.getSession().setAttribute(ERROR_ATTR, error);

            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_COURSES_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap()
                    ));
        } catch (ServiceException | UtilException | NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
