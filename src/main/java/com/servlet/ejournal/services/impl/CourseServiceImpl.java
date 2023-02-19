package com.servlet.ejournal.services.impl;

import com.servlet.ejournal.annotations.Transaction;
import com.servlet.ejournal.annotations.handlers.TransactionHandler;
import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.impl.*;
import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.*;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.services.dto.FullCourseDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.servlet.ejournal.utils.FullCourseUtil.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.utils.ValidationUtil.*;
import static com.servlet.ejournal.exceptions.ValidationError.*;


// TODO : on topic | teacher delete do something with courses (delete them, update, idk)
@Log4j2
public class CourseServiceImpl implements CourseService {
    private static CourseServiceImpl instance;
    private final HikariDataSource source;
    private final DAO<Course> courseDAO;
    private final DAO<Topic> topicDAO;
    private final DAO<User> userDAO;
    private final DAO<TopicCourse> topicCourseDAO;
    private final DAO<UserCourse> userCourseDAO;
    private final DAO<TeacherCourse> teacherCourseDAO;
    private final TopicService topicService;
    private final UserService userService;

    // Suppress constructor
    private CourseServiceImpl(ApplicationContext context) {
        this.source = context.getDataSource();

        this.courseDAO = context.getCourseDAO();
        this.topicDAO = context.getTopicDAO();
        this.userDAO = context.getUserDAO();
        this.topicCourseDAO = context.getTopicCourseDAO();
        this.userCourseDAO = context.getUserCourseDAO();
        this.teacherCourseDAO = context.getTeacherCourseDAO();

        this.topicService = context.getTopicService();
        this.userService = context.getUserService();
    }

    public static synchronized CourseService getInstance(ApplicationContext context) {
        if (instance == null) {
            instance = new CourseServiceImpl(context);
        }
        return instance;
    }

    @Override
    public List<FullCourseDTO> getAllCourses(int limit, int offset, String sorting, Map<String, String[]> filters) {
        try (Connection con = source.getConnection()) {
            return courseDAO.getAll(con, limit, offset, sorting, filters)
                    .stream()
                    .map(this::getCourseDTO)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<FullCourseDTO> getCourseDTO(Course course) {
        try (Connection con = source.getConnection()) {

            Topic currentTopic = getCurrentTopic(con, course.getC_id(), topicCourseDAO, topicDAO);
            User currentTeacher = getCurrentTeacher(con, course.getC_id(), teacherCourseDAO, userDAO);
            Map<String, String[]> filters = new HashMap<>();
            filters.put(AttributeConstants.COURSE_ID, new String[]{String.valueOf(course.getC_id())});

            FullCourseDTO courseDTO = new FullCourseDTO(
                    course.getC_id(),
                    currentTopic.getT_id(),
                    currentTeacher.getU_id(),
                    course.getName(),
                    course.getDescription(),
                    course.getStart_date(),
                    course.getEnd_date(),
                    getDuration(course.getStart_date(), course.getEnd_date()),
                    getCourseCount(filters),
                    topicService.getAllTopics(),
                    currentTopic.getName(),
                    currentTopic.getDescription(),
                    userService.getAllUsers(UserType.TEACHER),
                    String.format("%s %s", currentTeacher.getFirst_name(), currentTeacher.getLast_name())
            );
            return Optional.of(courseDTO);
        } catch (DAOException | UtilException | SQLException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
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
    public ValidationError updateCourse(FullCourseDTO courseDTO) throws ServiceException {
        try {
            TransactionHandler handler = TransactionHandler.getInstance(source);
            return handler.runTransaction(this, "updateCourseTransaction", courseDTO);
        } catch (TransactionException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't run transaction (update course)!");
        }
    }

    @Override
    public ValidationError createCourse(FullCourseDTO courseDTO) throws ServiceException {
        try {
            TransactionHandler handler = TransactionHandler.getInstance(source);
            return handler.runTransaction(this, "createCourseTransaction", courseDTO);
        } catch (TransactionException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Can't run transaction (create course)!");
        }
    }

    @Override
    public long deleteCourse(long id) throws ServiceException {
        try (Connection con = source.getConnection()) {
            return courseDAO.delete(con, id);
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public ValidationError enrollStudent(long userId, long courseId) {
        try (Connection con = source.getConnection()) {
            userCourseDAO.save(con, new UserCourse(0, userId, courseId, new Timestamp(System.currentTimeMillis()), 0));
            return NONE;
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            return DB_ERROR;
        }
    }

    @Override
    public ValidationError withdrawStudent(long userId, long courseId) {
        try (Connection con = source.getConnection()) {
            UserCourseDAO dao = (UserCourseDAO) userCourseDAO;
            Optional<UserCourse> uc = dao.get(con, courseId, userId);
            if (uc.isPresent()) {
                userCourseDAO.delete(con, uc.get().getU_c_id());
                return NONE;
            }
            return DB_ERROR;
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            return DB_ERROR;
        }
    }

    @Override
    public double getStudentMark(long courseId, long userId) {
        try (Connection con = source.getConnection()) {
            UserCourseDAO ucDao = (UserCourseDAO) userCourseDAO;
            Optional<UserCourse> uc = ucDao.get(con, courseId, userId);
            return uc.map(UserCourse::getFinal_mark).orElseThrow(DAOException::new);
        } catch (DAOException | SQLException e) {
            log.error(String.format("No such user-course relation (c_id), (u_id): %s, %s", courseId, userId));
            return 0;
        }
    }

    @Override
    public ValidationError updateStudentMark(UserCourse userCourse, double newMark) {
        try (Connection con = source.getConnection()) {
            userCourse.setFinal_mark(newMark);
            userCourseDAO.update(con, userCourse);
            return NONE;
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            return DB_ERROR;
        }
    }

    @Override
    public Optional<Course> getCourse(long id) {
        try (Connection con = source.getConnection()) {
            return courseDAO.get(con, id);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public int getCourseCount(Map<String, String[]> filters) {
        try (Connection con = source.getConnection()) {
            return getRecordsCount(
                    con,
                    String.format("%s.%s", AttributeConstants.COURSE_TABLE, AttributeConstants.COURSE_ID),
                    SQLQueries.JOIN_COURSE_TOPIC_USER_TEACHER_TABLE, filters);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    // Transaction logic
    @Transaction
    private ValidationError createCourseLogic(Connection con, FullCourseDTO courseDTO) throws ServiceException {
        Course course = getCourseFromDTO(courseDTO);
        try {
            ValidationError error = validateEndDate(courseDTO.getStartDate(), courseDTO.getEndDate());
            if (error.equals(NONE)) {
                long generatedId = courseDAO.save(con, course);
                userCourseDAO.save(con, new UserCourse(
                        0,
                        courseDTO.getCurrentTeacherId(),
                        generatedId,
                        new Timestamp(System.currentTimeMillis()),
                        -1
                ));
                teacherCourseDAO.save(con, new TeacherCourse(0, courseDTO.getCurrentTeacherId(), generatedId));
                topicCourseDAO.save(con, new TopicCourse(courseDTO.getCurrentTopicId(), generatedId));
            }
            return error;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Transaction
    private ValidationError updateCourseTransaction(Connection con, FullCourseDTO courseDTO) throws ServiceException {
        try {
            ValidationError error = validateEndDate(courseDTO.getStartDate(), courseDTO.getEndDate());
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
            }
            return error;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
