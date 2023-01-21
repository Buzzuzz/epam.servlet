package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.utils.FullCourseUtil;
import com.servlet.ejournal.utils.RequestBuilder;
import com.servlet.ejournal.utils.SqlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.services.impl.CourseServiceImpl;

import java.sql.Timestamp;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.utils.DateFormatterUtil.*;

@Log4j2
public class UpdateCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            Timestamp start = getTimestamp(req.getParameter(COURSE_START_DATE));
            Timestamp end = getTimestamp(req.getParameter(COURSE_END_DATE));

            FullCourseDTO courseDTO = new FullCourseDTO(
                    Long.parseLong(req.getParameter(COURSE_ID)),
                    Long.parseLong(req.getParameter(TOPIC_ID)),
                    Long.parseLong(req.getParameter(USER_ID)),
                    req.getParameter(COURSE_NAME),
                    req.getParameter(COURSE_DESCRIPTION),
                    start, end,
                    FullCourseUtil.getDuration(start, end),
                    service.getCourseCount(SqlUtil.getFilters(req, COURSE_ID)),
                    null,
                    null,
                    null,
                    null,
                    null
            );

            req.getSession().setAttribute(ERROR_ATTR, service.updateCourse(courseDTO));

            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.COURSE_DETAILS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            COURSE_ID));
        } catch (ServiceException | UtilException | NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
