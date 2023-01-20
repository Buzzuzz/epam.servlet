package com.my.project.controller.commands.impl.course;

import com.my.project.constants.PageConstants;
import com.my.project.model.entities.*;
import com.my.project.services.CourseService;
import com.my.project.services.dto.FullCourseDTO;
import com.my.project.services.impl.*;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.utils.FullCourseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.impl.UserServiceImpl;

import java.util.*;

import static com.my.project.utils.SqlUtil.*;
import static com.my.project.constants.AttributeConstants.*;
import static com.my.project.constants.CommandNameConstants.*;

@Log4j2
public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            // Pagination setup
            int limit = getLimit(req);
            String sorting = getSortingType(req, Course.class);

            // Filters setup
            String teacherFilter = getFilter(req, USER_ID);
            String topicFilter = getFilter(req, TOPIC_ID);
            String endDateFilter = getFilter(req, END_DATE_FILTER);
            String switchPosition = getFilter(req, SWITCH);
            Map<String, String[]> filters = getFilters(req, USER_ID, TOPIC_ID);
            getEndDateFilter(endDateFilter, filters);
            getMyCourseFilter(req, currentUser.getU_id(), filters);

            // Pagination setup (continuation)
            int[] pages = getPages(limit, service.getCourseCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            List<FullCourseDTO> courseList = service.getAllCourses(
                    limit,
                    offset,
                    sorting.contains(ENROLL_COMMAND) ? COURSE_ID : sorting,
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
            req.setAttribute(TOPICS_ATTR, TopicServiceImpl.getInstance().getAllTopics());
            req.setAttribute(TEACHERS_ATTR, UserServiceImpl.getInstance().getAllUsers(UserType.TEACHER));
            req.setAttribute(ERROR_ATTR, req.getAttribute(ERROR_ATTR));
            req.setAttribute(COURSES_ATTR, courseList);

            return PageConstants.COURSES_PAGE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
