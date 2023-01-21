package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.DAO;
import com.servlet.ejournal.model.entities.TeacherCourse;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.model.entities.TopicCourse;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FullCourseUtil {
    // Suppress constructor
    private FullCourseUtil() {
    }

    public static Topic getCurrentTopic(Connection con, long courseId, DAO<TopicCourse> topicCourseDAO, DAO<Topic> topicDAO) throws DAOException {
        Optional<TopicCourse> tco = topicCourseDAO.get(con, courseId);
        if (tco.isPresent()) {
            return topicDAO.get(con, tco.get().getT_id()).orElseThrow(() -> new DAOException("No such topic!"));
        }
        throw new DAOException("No such topic!");
    }

    public static User getCurrentTeacher(Connection con, long courseId, DAO<TeacherCourse> teacherCourseDAO, DAO<User> userDAO) throws DAOException {
        Optional<TeacherCourse> tco = teacherCourseDAO.get(con, courseId);
        if (tco.isPresent()) {
            return userDAO.get(con, tco.get().getTch_id()).orElseThrow(() ->
                    new DAOException(String.format("No teacher-course relation (c_id): %s", courseId)));
        }
        throw new DAOException(String.format("No teacher-course relation (c_id): %s", courseId));
    }

    public static long getDuration(Timestamp start, Timestamp end) {
        long millis = Duration.between(start.toLocalDateTime(), end.toLocalDateTime()).toMillis();
        return TimeUnit.MILLISECONDS.toDays(millis) + 1;
    }

    public static List<FullCourseDTO> sortByEnrolledStudents(String sorting, List<FullCourseDTO> courseList) {
        if (sorting.contains(CommandNameConstants.ENROLL_COMMAND)) {
            courseList = courseList
                    .stream()
                    .sorted(Comparator.comparingLong(FullCourseDTO::getEnrolled))
                    .collect(Collectors.toList());
            if (sorting.equals(AttributeConstants.ENROLLED_DESC_SORTING)) {
                Collections.reverse(courseList);
            }
        }
        return courseList;
    }
}
