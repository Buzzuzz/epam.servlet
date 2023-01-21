package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.utils.FullCourseUtil;
import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.model.entities.Course;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import com.servlet.ejournal.services.impl.CourseServiceImpl;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.impl.UserServiceImpl;

import java.util.*;

import static com.servlet.ejournal.utils.SqlUtil.*;

@Log4j2
public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            // Pagination setup
            int limit = getLimit(req);
            String sorting = getSortingType(req, Course.class);

            // Filters setup
            String teacherFilter = getFilter(req, AttributeConstants.USER_ID);
            String topicFilter = getFilter(req, AttributeConstants.TOPIC_ID);
            String endDateFilter = getFilter(req, AttributeConstants.END_DATE_FILTER);
            String switchPosition = getFilter(req, AttributeConstants.SWITCH);
            Map<String, String[]> filters = getFilters(req, AttributeConstants.USER_ID, AttributeConstants.TOPIC_ID);
            getEndDateFilter(endDateFilter, filters);
            getMyCourseFilter(req, currentUser.getU_id(), filters);

            // Pagination setup (continuation)
            int[] pages = getPages(limit, service.getCourseCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            List<FullCourseDTO> courseList = service.getAllCourses(
                    limit,
                    offset,
                    sorting.contains(CommandNameConstants.ENROLL_COMMAND) ? AttributeConstants.COURSE_ID : sorting,
                    filters);

            courseList = FullCourseUtil.sortByEnrolledStudents(sorting, courseList);

            req.setAttribute(AttributeConstants.SWITCH, switchPosition);
            req.setAttribute(AttributeConstants.SORTING_TYPE, sorting);
            req.setAttribute(AttributeConstants.DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(AttributeConstants.CURRENT_PAGE, currentPage);
            req.setAttribute(AttributeConstants.RECORDS, pages);
            req.setAttribute(AttributeConstants.TOPIC_ID, topicFilter);
            req.setAttribute(AttributeConstants.USER_ID, teacherFilter);
            req.setAttribute(AttributeConstants.END_DATE_FILTER, endDateFilter);
            req.setAttribute(AttributeConstants.TOPICS_ATTR, TopicServiceImpl.getInstance().getAllTopics());
            req.setAttribute(AttributeConstants.TEACHERS_ATTR, UserServiceImpl.getInstance().getAllUsers(UserType.TEACHER));
            req.setAttribute(AttributeConstants.ERROR_ATTR, req.getAttribute(AttributeConstants.ERROR_ATTR));
            req.setAttribute(AttributeConstants.COURSES_ATTR, courseList);

            return PageConstants.COURSES_PAGE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
