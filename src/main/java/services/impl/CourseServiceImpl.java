package services.impl;

import exceptions.*;
import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import model.dao.DataSource;
import model.dao.impl.*;
import model.entities.*;
import services.*;
import services.dto.FullCourseDTO;


import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

import static model.dao.DataSource.*;
import static exceptions.ErrorType.*;
import static utils.PaginationUtil.*;
import static constants.AttributeConstants.*;
import static utils.ValidationUtil.*;


@Log4j2
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
                                        TOPIC_ID,
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
                                        USER_ID,
                                        new HashMap<String, String>() {{
                                            put(USER_TYPE_DB, "'" + UserType.TEACHER.name() + "'");
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
    public Course getCourseFromDTO(FullCourseDTO courseDTO) {
        return new Course(
                courseDTO.getCourseId(),
                courseDTO.getCourseName(),
                courseDTO.getCourseDescription(),
                courseDTO.getStartDate(),
                courseDTO.getEndDate()
        );
    }

    @Override
    public ErrorType updateCourse(FullCourseDTO courseDTO) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            setAutoCommit(con, false);

            ErrorType error = validateEndDate(courseDTO.getStartDate(), courseDTO.getEndDate());

            if (error.equals(NONE)) {
                UserCourse uc = userCourseDAO.get(con, courseDTO.getCourseId()).get();
                TopicCourse tc = topicCourseDAO.get(con, courseDTO.getCourseId()).get();

                uc.setU_id(courseDTO.getCurrentTeacherId());
                tc.setT_id(courseDTO.getCurrentTopicId());
                userCourseDAO.update(con, uc);
                topicCourseDAO.update(con, tc);

                courseDAO.update(con, getCourseFromDTO(courseDTO));
                commit(con);
            }

            setAutoCommit(con, true);
            return error;
        } catch (DAOException e) {
            rollback(con);
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            close(con);
        }
    }

    @Override
    public ErrorType createCourse(FullCourseDTO courseDTO) throws ServiceException {
        Connection con = null;
        Course course = getCourseFromDTO(courseDTO);

        try {
            con = getConnection();
            setAutoCommit(con, false);

            ErrorType error = validateEndDate(courseDTO.getStartDate(), courseDTO.getEndDate());
            if (error.equals(NONE)) {
                long generatedId = courseDAO.save(con, course);

                userCourseDAO.save(con, new UserCourse(
                        0,
                        courseDTO.getCurrentTeacherId(),
                        generatedId,
                        courseDTO.getStartDate(),
                        0
                ));

                topicCourseDAO.save(con, new TopicCourse(
                        courseDTO.getCurrentTopicId(),
                        generatedId
                ));

                commit(con);
            }

            setAutoCommit(con, true);
            return error;
        } catch (DAOException e) {
            rollback(con);
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            close(con);
        }
    }

    @Override
    public long deleteCourse(long id) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return courseDAO.delete(con, id);
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            close(con);
        }
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

    @Override
    public int getCourseCount() {
        Connection con = null;
        try {
            con = getConnection();
            return getRecordsCount(con, COURSE_ID, COURSE_TABLE);
        } finally {
            close(con);
        }
    }
}
