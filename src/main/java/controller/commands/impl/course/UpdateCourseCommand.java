package controller.commands.impl.course;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.CourseService;
import services.dto.FullCourseDTO;
import services.impl.CourseServiceImpl;
import utils.*;

import java.sql.Timestamp;

import static constants.AttributeConstants.*;
import static utils.DateFormatterUtil.*;

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
