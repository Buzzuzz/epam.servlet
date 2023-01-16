package utils;

import exceptions.DAOException;
import model.dao.DAO;
import model.dao.impl.TopicCourseDAO;
import model.dao.impl.TopicDAO;
import model.entities.*;
import services.TopicService;
import services.UserService;
import services.dto.TopicDTO;
import services.dto.UserDTO;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static constants.AttributeConstants.*;

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

    public static User getCurrentTeacher(Connection con, long courseId, DAO<UserCourse> userCourseDAO, DAO<User> userDAO) throws DAOException {
        Optional<UserCourse> uco = userCourseDAO.get(con, courseId);
        if (uco.isPresent()) {
            return userDAO.get(con, uco.get().getU_id()).orElseThrow(() ->
                    new DAOException(String.format("No teacher-course relation (c_id): %s", courseId)));
        }
        throw new DAOException(String.format("No teacher-course relation (c_id): %s", courseId));
    }

    public static List<UserDTO> getTeachersList(Connection con, DAO<User> userDAO, UserService userService) {
        return userDAO.getAll(
                        con,
                        userService.getUserCount(null),
                        0,
                        USER_ID,
                        new HashMap<String, String[]>() {{
                            put(USER_TYPE_DB, new String[]{UserType.TEACHER.name()});
                        }})
                .stream()
                .map(userService::getUserDTO)
                .collect(Collectors.toList());
    }

    public static List<TopicDTO> getTopicsList(Connection con, DAO<Topic> topicDAO, TopicService topicService) {
        return topicDAO
                .getAll(
                        con,
                        topicService.getTopicCount(),
                        0,
                        TOPIC_ID,
                        null)
                .stream()
                .map(topicService::getTopicDTO)
                .collect(Collectors.toList());
    }

    public static long getDuration(Timestamp start, Timestamp end) {
        long millis = Duration.between(start.toLocalDateTime(), end.toLocalDateTime()).toMillis();
        return TimeUnit.MILLISECONDS.toDays(millis) + 1;
    }
}
