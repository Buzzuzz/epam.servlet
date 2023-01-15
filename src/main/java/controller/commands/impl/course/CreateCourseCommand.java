package controller.commands.impl.course;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import exceptions.ServiceException;
import exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.CourseService;
import services.dto.FullCourseDTO;
import services.impl.CourseServiceImpl;
import utils.DateFormatterUtil;
import utils.RequestBuilder;

import static constants.CommandNameConstants.*;
import static constants.AttributeConstants.*;

@Log4j2
public class CreateCourseCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            FullCourseDTO courseDTO = new FullCourseDTO(
                    0,
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

            ErrorType error = service.createCourse(courseDTO);
            req.getSession().setAttribute(ERROR_ATTR, error);

            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    GET_ALL_COURSES_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap()
                    ));
        } catch (ServiceException | UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
