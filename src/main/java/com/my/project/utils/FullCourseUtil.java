package com.my.project.utils;

import com.my.project.exceptions.DAOException;
import com.my.project.model.dao.DAO;
import com.my.project.model.entities.TeacherCourse;
import com.my.project.model.entities.Topic;
import com.my.project.model.entities.TopicCourse;
import com.my.project.model.entities.User;
import com.my.project.model.entities.*;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
}
