package controller.commands.impl.course;

import constants.CommandNameConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.Course;
import services.CourseService;
import services.impl.CourseServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static constants.AttributeConstants.*;
import static utils.PaginationUtil.*;

@Log4j2
public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            int limit = getLimit(req);
            String sorting = getSortingType(req, Course.class);
            String teacherFilter = getFilter(req, USER_ID);
            String topicFilter = getFilter(req, TOPIC_ID);
            Map<String, String[]> filters = new HashMap<>();

            if (!teacherFilter.equals(NONE_ATTR)) {
                filters.put(String.format("%s.%s", USER_COURSE_TABLE, USER_ID), new String[]{teacherFilter});
            }
            if (!topicFilter.equals(NONE_ATTR)) {
                filters.put(String.format("%s.%s", TOPIC_COURSE_TABLE, TOPIC_ID), new String[]{topicFilter});
            }

            int[] pages = getPages(limit, service.getCourseCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, pages);
            req.setAttribute(TOPIC_ID, topicFilter);
            req.setAttribute(USER_ID, teacherFilter);

            req.setAttribute(COURSES_ATTR, service.getAllCourses(limit, pages, currentPage, offset, sorting, filters));
            return PageConstants.COURSES_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
