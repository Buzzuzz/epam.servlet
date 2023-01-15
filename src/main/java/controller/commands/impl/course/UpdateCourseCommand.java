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

import static constants.AttributeConstants.*;

@Log4j2
public class UpdateCourseCommand implements Command {
    // TODO implement
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();

            FullCourseDTO courseDTO = new FullCourseDTO(
                    Long.parseLong(req.getParameter(COURSE_ID)),
                    Long.parseLong(req.getParameter(TOPIC_ID)),
                    Long.parseLong(req.getParameter(USER_ID)),
                    req.getParameter(COURSE_NAME),
                    req.getParameter(COURSE_DESCRIPTION),
                    DateFormatterUtil.getTimestamp(req.getParameter(COURSE_START_DATE)),
                    DateFormatterUtil.getTimestamp(req.getParameter(COURSE_END_DATE)),
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
