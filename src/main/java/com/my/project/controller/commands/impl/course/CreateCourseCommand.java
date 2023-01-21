package com.my.project.controller.commands.impl.course;

import com.my.project.constants.CommandNameConstants;
import com.my.project.exceptions.ValidationError;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.utils.DateFormatterUtil;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.CourseService;
import com.my.project.services.dto.FullCourseDTO;
import com.my.project.utils.*;

import static com.my.project.constants.AttributeConstants.*;

import java.sql.Timestamp;

@Log4j2
public class CreateCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
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
