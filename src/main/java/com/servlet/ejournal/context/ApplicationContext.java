package com.servlet.ejournal.context;

import com.servlet.ejournal.exceptions.ApplicationContextException;
import com.servlet.ejournal.exceptions.ConfigException;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.impl.*;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.dao.interfaces.IntermediateTable;
import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.CourseServiceImpl;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

@Log4j2
@Getter
@SuppressWarnings("unchecked")
public class ApplicationContext {
    // Current classpath path
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static ApplicationContext context;
    private final HikariDataSource dataSource;
    private final DAO<Course> courseDAO;
    private final DAO<User> userDAO;
    private final DAO<Topic> topicDAO;
    private final DAO<TopicCourse> topicCourseDAO;
    private final DAO<UserCourse> userCourseDAO;
    private final DAO<TeacherCourse> teacherCourseDAO;
    private final IntermediateTable<UserCourse> userCourseIntermediateTable;
    private final IntermediateTable<TeacherCourse> teacherCourseIntermediateTable;
    private final IntermediateTable<TopicCourse> topicCourseIntermediateTable;
    private final UserService userService;
    private final CourseService courseService;
    private final TopicService topicService;

    // Suppress constructor
    private ApplicationContext() throws ConfigException {
        log.info("Application context initialization...");
        this.dataSource = HikariDataSource.getInstance(getDataBaseConfigPath());

        this.courseDAO = CourseDAO.getInstance(dataSource);
        this.userDAO = UserDAO.getInstance(dataSource);
        this.topicDAO = TopicDAO.getInstance(dataSource);
        this.topicCourseDAO = TopicCourseDAO.getInstance(dataSource);
        this.userCourseDAO = UserCourseDAO.getInstance(dataSource);
        this.teacherCourseDAO = TeacherCourseDAO.getInstance(dataSource);
        this.userCourseIntermediateTable = (IntermediateTable<UserCourse>) userCourseDAO;
        this.teacherCourseIntermediateTable = (IntermediateTable<TeacherCourse>) teacherCourseDAO;
        this.topicCourseIntermediateTable = (IntermediateTable<TopicCourse>) topicCourseDAO;

        this.userService = UserServiceImpl.getInstance(this);
        this.courseService = CourseServiceImpl.getInstance(this);
        this.topicService = TopicServiceImpl.getInstance(this);

        log.info("Application context initialized!");
    }

    public static synchronized ApplicationContext getInstance() throws ApplicationContextException {
        if (context == null) {
            try {
                context = new ApplicationContext();
            } catch (ConfigException e) {
                log.fatal(e.getMessage());
                throw new ApplicationContextException(e.getMessage(), e);
            }
        }
        return context;
    }

    private String getDataBaseConfigPath() throws ConfigException {
        try (InputStream source = classLoader.getResourceAsStream("props.properties")) {
            Properties props = new Properties();
            props.load(source);
            if (props.getProperty("db.path").length() != 0) {
                return Objects.requireNonNull(classLoader.getResource(props.getProperty("db.path"))).getPath();
            }
            String msg = "Main configuration file (props.properties) does not contain db.path!";
            log.fatal(msg);
            throw new ConfigException(msg);
        } catch (IOException e) {
            log.fatal("Can' read file props.properties!", e);
            throw new ConfigException("Can't read main configuration file (props.properties)!", e);
        }
    }
}
