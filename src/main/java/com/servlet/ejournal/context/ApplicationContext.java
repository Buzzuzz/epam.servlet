package com.servlet.ejournal.context;

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

@Log4j2
@Getter
@SuppressWarnings("unchecked")
public class ApplicationContext {
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
    private ApplicationContext() {
        log.info("Application context initialization...");

        this.courseDAO = CourseDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
        this.topicDAO = TopicDAO.getInstance();
        this.topicCourseDAO = TopicCourseDAO.getInstance();
        this.userCourseDAO = UserCourseDAO.getInstance();
        this.teacherCourseDAO = TeacherCourseDAO.getInstance();
        this.userCourseIntermediateTable = (IntermediateTable<UserCourse>) userCourseDAO;
        this.teacherCourseIntermediateTable = (IntermediateTable<TeacherCourse>) teacherCourseDAO;
        this.topicCourseIntermediateTable = (IntermediateTable<TopicCourse>) topicCourseDAO;

        this.userService = UserServiceImpl.getInstance();
        this.courseService = CourseServiceImpl.getInstance();
        this.topicService = TopicServiceImpl.getInstance();

        log.info("Application context initialized!");
    }

    private static class Holder {
        private static final ApplicationContext context = new ApplicationContext();
    }

    public static ApplicationContext getInstance() {
        return Holder.context;
    }
}
