package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.utils.FullCourseUtil;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.util.*;

import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
            CourseService courseService = context.getCourseService();
            TopicService topicService = context.getTopicService();
            UserService userService = context.getUserService();
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            // Pagination setup
            int limit = getLimit(req);
            String sorting = getSortingType(req, DEFAULT_COURSE_SORTING);

            // Filters setup
            String teacherFilter = getFilter(req, USER_ID);
            String topicFilter = getFilter(req, TOPIC_ID);
            String endDateFilter = getFilter(req, END_DATE_FILTER);
            String switchPosition = getFilter(req, SWITCH);
            Map<String, String[]> filters = getFilters(req, USER_ID, TOPIC_ID);
            getEndDateFilter(endDateFilter, filters);
            getMyCourseFilter(req, currentUser.getU_id(), filters);

            // Pagination setup (continuation)
            int[] pages = getPages(limit, courseService.getCourseCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            List<FullCourseDTO> courseList = courseService.getAllCourses(
                    limit,
                    offset,
                    sorting.contains(CommandNameConstants.ENROLL_COMMAND) ? COURSE_ID : sorting,
                    filters);

            courseList = FullCourseUtil.sortByEnrolledStudents(sorting, courseList);

            req.setAttribute(SWITCH, switchPosition);
            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, pages);
            req.setAttribute(TOPIC_ID, topicFilter);
            req.setAttribute(USER_ID, teacherFilter);
            req.setAttribute(END_DATE_FILTER, endDateFilter);
            req.setAttribute(TOPICS_ATTR, topicService.getAllTopics());
            req.setAttribute(TEACHERS_ATTR, userService.getAllUsers(UserType.TEACHER));
            req.setAttribute(ERROR_ATTR, req.getAttribute(ERROR_ATTR));
            req.setAttribute(COURSES_ATTR, courseList);

            return PageConstants.COURSES_PAGE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
