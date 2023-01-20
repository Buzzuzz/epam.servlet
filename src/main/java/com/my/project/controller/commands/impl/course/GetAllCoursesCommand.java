package com.my.project.controller.commands.impl.course;

import com.my.project.constants.PageConstants;
import com.my.project.model.entities.Course;
import com.my.project.model.entities.User;
import com.my.project.model.entities.UserType;
import com.my.project.services.CourseService;
import com.my.project.services.dto.FullCourseDTO;
import com.my.project.services.impl.CourseServiceImpl;
import com.my.project.services.impl.TopicServiceImpl;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.impl.UserServiceImpl;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.my.project.utils.PaginationUtil.*;
import static com.my.project.constants.AttributeConstants.*;

// TODO : refactor this piece of ... code
@Log4j2
public class GetAllCoursesCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            CourseService service = CourseServiceImpl.getInstance();
            User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            int limit = getLimit(req);
            String sorting = getSortingType(req, Course.class);
            String teacherFilter = getFilter(req, USER_ID);
            String topicFilter = getFilter(req, TOPIC_ID);
            String endDateFilter = getFilter(req, END_DATE_FILTER);
            String switchPosition = getFilter(req, SWITCH);
            Map<String, String[]> filters = new HashMap<>();

            String teacherFilterString = String.format("%s.%s", TEACHER_COURSE_TABLE, TEACHER_ID);
            String userFilterString = String.format("%s.%s", USER_COURSE_TABLE, USER_ID);

            if (!teacherFilter.equals(NONE_ATTR)) {
                filters.put(teacherFilterString, new String[]{teacherFilter});
            }
            if (!topicFilter.equals(NONE_ATTR)) {
                filters.put(String.format("%s.%s", TOPIC_COURSE_TABLE, TOPIC_ID), new String[]{topicFilter});
            }
            if (!switchPosition.equals(NONE_ATTR)) {
                filters.put(userFilterString, new String[]{String.valueOf(currentUser.getU_id())});
            } else {
                filters.put(FINAL_MARK, new String[]{"-1"});
            }

            int[] pages = getPages(limit, service.getCourseCount(filters));
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);

            List<FullCourseDTO> temp = service.getAllCourses(limit, offset, sorting, filters);
            log.fatal(temp);

            switch (sorting) {
                case ENROLLED_ASC_SORTING:
                    temp = temp
                            .stream()
                            .sorted(Comparator.comparingLong(FullCourseDTO::getEnrolled))
                            .collect(Collectors.toList());
                    break;
                case ENROLLED_DESC_SORTING:
                    temp = temp
                            .stream()
                            .sorted(Comparator.comparingLong(FullCourseDTO::getEnrolled).reversed())
                            .collect(Collectors.toList());
                    break;
            }

            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            switch (endDateFilter) {
                case COURSE_NOT_STARTED:
                    temp = temp
                            .stream()
                            .filter(dto -> dto.getStartDate().getTime() > currentDate.getTime())
                            .collect(Collectors.toList());
                    break;
                case COURSE_IN_PROGRESS:
                    temp = temp
                            .stream()
                            .filter(dto -> dto.getStartDate().getTime() < currentDate.getTime())
                            .filter(dto -> dto.getEndDate().getTime() > currentDate.getTime())
                            .collect(Collectors.toList());
                    break;
                case COURSE_ENDED:
                    temp = temp
                            .stream()
                            .filter(dto -> dto.getEndDate().getTime() < new Timestamp(System.currentTimeMillis()).getTime())
                            .collect(Collectors.toList());
                    break;
            }

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
            req.setAttribute(COURSES_ATTR, temp);

            return PageConstants.COURSES_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
