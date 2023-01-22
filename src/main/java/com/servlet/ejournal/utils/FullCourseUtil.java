package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.UtilException;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FullCourseUtil {
    // Suppress constructor
    private FullCourseUtil() {
    }

    public static Topic getCurrentTopic(Connection con, long courseId, DAO<TopicCourse> topicCourseDAO, DAO<Topic> topicDAO) throws DAOException {
        try {
            Optional<TopicCourse> tco = topicCourseDAO.get(con, courseId);
            if (tco.isPresent()) {
                return topicDAO.get(con, tco.get().getT_id()).orElseThrow(() -> new DAOException("No such topic!"));
            }
            throw new DAOException("No such topic!");
        } catch (Exception e) {
            throw new DAOException("No such topic-course relation!");
        }
    }

    public static User getCurrentTeacher(Connection con, long courseId, DAO<TeacherCourse> teacherCourseDAO, DAO<User> userDAO) throws DAOException {
        try {
            DAOException e = new DAOException(String.format("No teacher-course relation (c_id): %s", courseId));
            Optional<TeacherCourse> tco = teacherCourseDAO.get(con, courseId);
            if (tco.isPresent()) {
                return userDAO.get(con, tco.get().getTch_id()).orElseThrow(() -> e);
            }
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public static long getDuration(Timestamp start, Timestamp end) throws UtilException {
        if (start == null || end == null) throw new UtilException("One of parameters is null!");
        long millis = Duration.between(start.toLocalDateTime(), end.toLocalDateTime()).toMillis();
        return TimeUnit.MILLISECONDS.toDays(millis) + 1;
    }

    public static List<FullCourseDTO> sortByEnrolledStudents(String sorting, List<FullCourseDTO> courseList) {
        if (sorting == null) return courseList;
        if (courseList == null) return new ArrayList<>();
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
