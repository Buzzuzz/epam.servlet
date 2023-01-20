package com.my.project.controller.commands.impl.course;

import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import com.my.project.exceptions.UtilException;
import com.my.project.utils.FullCourseUtil;
import com.my.project.utils.RequestBuilder;
import com.my.project.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.CourseService;
import com.my.project.services.dto.FullCourseDTO;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.utils.*;

import java.sql.Timestamp;
import java.util.HashMap;

import static com.my.project.constants.AttributeConstants.*;
import static com.my.project.utils.DateFormatterUtil.*;

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
                    service.getCourseCount(new HashMap<String, String[]>() {{
                        put(COURSE_ID, new String[]{req.getParameter(COURSE_ID)});
                    }}),
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
        } catch (ServiceException | UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
