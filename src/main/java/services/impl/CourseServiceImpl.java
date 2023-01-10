package services.impl;

import jakarta.servlet.http.HttpServletRequest;
import model.dao.DAO;
import model.dao.DataSource;
import model.dao.impl.*;
import model.entities.*;
import services.CourseService;
import services.dto.FullCourseDTO;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static model.dao.DataSource.*;

public class CourseServiceImpl implements CourseService {
    private static final DAO<Course> courseDAO = CourseDAO.getInstance();
    private static final DAO<Topic> topicDAO = TopicDAO.getInstance();
    private static final DAO<User> userDAO = UserDAO.getInstance();
    private static final DAO<TopicCourse> topicCourseDAO = TopicCourseDAO.getInstance();
    private static final DAO<UserCourse> userCourseDAO = UserCourseDAO.getInstance();

    // Suppress constructor
    private CourseServiceImpl() {
    }

    public static CourseService getInstance() {
        return Holder.service;
    }

    private static class Holder {
        private static final CourseService service = new CourseServiceImpl();
    }
    @Override
    public List<FullCourseDTO> getAllCourses(HttpServletRequest req) {
        Connection con = null;
        List<FullCourseDTO> transferList = new ArrayList<>();
        try {
            con = getConnection();

            List<Course> courseList = (List<Course>) courseDAO.getAll(con);
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
                    course.getName(),
                    course.getDescription(),
                    course.getStart_date(),
                    course.getEnd_date(),
                    "",
                    "",
                    ""
            );

            if (topicCourseDAO.get(con, course.getC_id()).isPresent() &&
                userCourseDAO.get(con, course.getC_id()).isPresent()) {
                TopicCourse topicCourse = topicCourseDAO.get(con, course.getC_id()).get();
                courseDTO.setTopicName(topicDAO.get(con, topicCourse.getT_id()).get().getName());

                UserCourse userCourse = userCourseDAO.get(con, course.getC_id()).get();
                courseDTO.setFirstName(userDAO.get(con, userCourse.getU_id()).get().getFirst_name());
                courseDTO.setLastName(userDAO.get(con, userCourse.getU_id()).get().getLast_name());

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
