package services.impl;

import constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.DAO;
import model.dao.DataSource;
import model.dao.impl.*;
import model.entities.*;
import services.CourseService;
import services.TopicService;
import services.UserService;
import services.dto.FullCourseDTO;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static model.dao.DataSource.*;

public class CourseServiceImpl implements CourseService {
    private static final DAO<Course> courseDAO = CourseDAO.getInstance();
    private static final DAO<Topic> topicDAO = TopicDAO.getInstance();
    private static final DAO<User> userDAO = UserDAO.getInstance();
    private static final DAO<TopicCourse> topicCourseDAO = TopicCourseDAO.getInstance();
    private static final DAO<UserCourse> userCourseDAO = UserCourseDAO.getInstance();
    private static final TopicService topicService = TopicServiceImpl.getInstance();
    private static final UserService userService = UserServiceImpl.getInstance();

    // Suppress constructor
    private CourseServiceImpl() {
    }

    public static CourseService getInstance() {
        return Holder.service;
    }

    private static class Holder {
        private static final CourseService service = new CourseServiceImpl();
    }

    // TODO: refactor getAll method
    @Override
    public List<FullCourseDTO> getAllCourses() {
        Connection con = null;
        List<FullCourseDTO> transferList = new ArrayList<>();
        try {
            con = getConnection();

            List<Course> courseList = (List<Course>) courseDAO.getAll(con, 0, 0, "", new HashMap<>());
            for (Course course : courseList) {
                getCourseDTO(course).ifPresent(transferList::add);
            }

            return transferList;
        } finally {
            DataSource.close(con);
        }
    }

    @Override
    public Optional<FullCourseDTO> getCourseDTO(Course course) {
        Connection con = null;

        try {
            con = getConnection();

            FullCourseDTO courseDTO = new FullCourseDTO(
                    course.getC_id(),
                    0,
                    0,
                    course.getName(),
                    course.getDescription(),
                    course.getStart_date(),
                    course.getEnd_date(),
                    new ArrayList<>(),
                    "",
                    "",
                    new ArrayList<>(),
                    ""
            );

            if (topicCourseDAO.get(con, course.getC_id()).isPresent() &&
                    userCourseDAO.get(con, course.getC_id()).isPresent()) {

                Connection finalCon = con;
                topicCourseDAO
                        .get(con, course.getC_id())
                        .ifPresent(tc -> {
                            Topic topic = topicDAO.get(finalCon, tc.getT_id()).get();
                            courseDTO.setCurrentTopicId(tc.getT_id());
                            courseDTO.setCurrentTopicName(topic.getName());
                            courseDTO.setCurrentTopicDescription(topic.getDescription());
                        });
                userCourseDAO
                        .get(con, course.getC_id())
                        .ifPresent(uc -> {
                            User user = userDAO.get(finalCon, uc.getU_id()).get();
                            courseDTO.setCurrentTeacherId(uc.getU_id());
                            courseDTO.setCurrentTeacherName(user.getFirst_name() + " " + user.getLast_name());
                        });
                courseDTO.setTopics(
                        topicDAO
                                .getAll(
                                        con,
                                        topicService.getTopicCount(),
                                        0,
                                        AttributeConstants.TOPIC_ID,
                                        new HashMap<>())
                                .stream()
                                .map(topicService::getTopicDTO)
                                .collect(Collectors.toList()));
                courseDTO.setTeachers(
                        userDAO
                                .getAll(
                                        con,
                                        userService.getUserCount(),
                                        0,
                                        AttributeConstants.USER_ID,
                                        new HashMap<String, String>() {{
                                            put(AttributeConstants.USER_TYPE_DB, "'" + UserType.TEACHER.name() + "'");
                                        }})
                                .stream()
                                .map(userService::getUserDTO)
                                .collect(Collectors.toList())
                );

                return Optional.of(courseDTO);
            }
        } finally {
            close(con);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Course> getCourse(long id) {
        Connection con = null;
        try {
            con = getConnection();
            return courseDAO.get(con, id);
        } finally {
            close(con);
        }
    }
}
