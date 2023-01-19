package services.impl;

import constants.SQLQueries;
import exceptions.*;
import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import model.dao.impl.*;
import model.entities.*;
import services.*;
import services.dto.FullCourseDTO;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static model.dao.DataSource.*;
import static exceptions.ErrorType.*;
import static utils.FullCourseUtil.*;
import static utils.PaginationUtil.*;
import static constants.AttributeConstants.*;
import static utils.ValidationUtil.*;


// TODO : on topic | teacher delete do something with courses (delete them, update, idk)
@Log4j2
public class CourseServiceImpl implements CourseService {
    private static final DAO<Course> courseDAO = CourseDAO.getInstance();
    private static final DAO<Topic> topicDAO = TopicDAO.getInstance();
    private static final DAO<User> userDAO = UserDAO.getInstance();
    private static final DAO<TopicCourse> topicCourseDAO = TopicCourseDAO.getInstance();
    private static final DAO<UserCourse> userCourseDAO = UserCourseDAO.getInstance();
    private static final DAO<TeacherCourse> teacherCourseDAO = TeacherCourseDAO.getInstance();
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

    @Override
    public List<FullCourseDTO> getAllCourses(int limit, int offset, String sorting, Map<String, String[]> filters) {
        Connection con = null;
        try {
            con = getConnection();
            return courseDAO.getAll(con, limit, offset, sorting, filters)
                    .stream()
                    .map(this::getCourseDTO)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } finally {
            close(con);
        }
    }

    @Override
    public Optional<FullCourseDTO> getCourseDTO(Course course) {
        Connection con = null;

        try {
            con = getConnection();
            Topic currentTopic = getCurrentTopic(con, course.getC_id(), topicCourseDAO, topicDAO);
            User currentTeacher = getCurrentTeacher(con, course.getC_id(), teacherCourseDAO, userDAO);

            FullCourseDTO courseDTO = new FullCourseDTO(
                    course.getC_id(),
                    currentTopic.getT_id(),
                    currentTeacher.getU_id(),
                    course.getName(),
                    course.getDescription(),
                    course.getStart_date(),
                    course.getEnd_date(),
                    getDuration(course.getStart_date(), course.getEnd_date()),
                    getCourseCount(new HashMap<String, String[]>() {{
                        put(COURSE_ID, new String[]{String.valueOf(course.getC_id())});
                    }}),
                    topicService.getAllTopics(),
                    currentTopic.getName(),
                    currentTopic.getDescription(),
                    userService.getAllUsers(UserType.TEACHER),
                    String.format("%s %s", currentTeacher.getFirst_name(), currentTeacher.getLast_name())
            );
            return Optional.of(courseDTO);
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        } finally {
            close(con);
        }
    }

    @Override
    public Course getCourseFromDTO(FullCourseDTO courseDTO) {
        return new Course(
                courseDTO.getCourseId(),
                courseDTO.getCourseName(),
                courseDTO.getCourseDescription(),
                courseDTO.getStartDate(),
                courseDTO.getEndDate(),
                courseDTO.getDuration()
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
                TeacherCourse tchC = teacherCourseDAO.get(con, courseDTO.getCourseId()).get();

                uc.setU_id(courseDTO.getCurrentTeacherId());
                tc.setT_id(courseDTO.getCurrentTopicId());
                tchC.setTch_id(courseDTO.getCurrentTeacherId());
                userCourseDAO.update(con, uc);
                teacherCourseDAO.update(con, tchC);
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
                        new Timestamp(System.currentTimeMillis()),
                        -1
                ));

                teacherCourseDAO.save(con, new TeacherCourse(
                        courseDTO.getCurrentTeacherId(),
                        generatedId
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
    public ErrorType enrollStudent(long userId, long courseId) {
        Connection con = null;
        try {
            con = getConnection();
            userCourseDAO.save(con, new UserCourse(0, userId, courseId, new Timestamp(System.currentTimeMillis()), 0));
            return NONE;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return ErrorType.DB_ERROR;
        } finally {
            close(con);
        }
    }

    @Override
    public ErrorType withdrawStudent(long userId, long courseId) {
        Connection con = null;
        try {
            con = getConnection();
            UserCourseDAO dao = (UserCourseDAO) userCourseDAO;
            Optional<UserCourse> uc = dao.get(con, courseId, userId);
            if (uc.isPresent()) {
                userCourseDAO.delete(con, uc.get().getU_c_id());
                return NONE;
            }
            return ErrorType.DB_ERROR;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return ErrorType.DB_ERROR;
        } finally {
            close(con);
        }
    }

    @Override
    public ErrorType updateStudentMark(UserCourse userCourse, double newMark) {
        Connection con = null;
        try {
            con = getConnection();

            userCourse.setFinal_mark(newMark);
            userCourseDAO.update(con, userCourse);

            return NONE;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return ErrorType.DB_ERROR;
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
    public int getCourseCount(Map<String, String[]> filters) {
        Connection con = null;
        try {
            con = getConnection();
            return getRecordsCount(
                    con,
                    String.format("%s.%s", COURSE_TABLE, COURSE_ID),
                    SQLQueries.JOIN_COURSE_TOPIC_USER_TEACHER_TABLE, filters);
        } finally {
            close(con);
        }
    }
}
