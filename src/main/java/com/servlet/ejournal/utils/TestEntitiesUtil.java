package com.servlet.ejournal.utils;

import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.services.dto.UserCourseDTO;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.services.impl.UserServiceImpl;

import java.sql.Timestamp;

import java.util.Collections;

import static com.servlet.ejournal.services.UserService.getUserDTO;

/**
 * Class for producing test {@link com.servlet.ejournal.model.entities Entites} and
 * {@link com.servlet.ejournal.services.dto DTOs} for tests
 */
public class TestEntitiesUtil {
    private TestEntitiesUtil() {
    }

    public static User createTestUser() {
        return new User(
                10,
                "test@mail.com",
                "password",
                "Billy",
                "Harrington",
                "123456789",
                UserType.STUDENT,
                false,
                false
        );
    }

    public static Topic createTestTopic() {
        return new Topic(1, "Rick Astley", "Never Gonna Give You Up!");
    }

    public static Course createTestCourse() {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        return new Course(
                15,
                "I'm an artist",
                "Performance artist...",
                date,
                date,
                1
        );
    }

    public static UserCourse createTestUserCourse() {
        return new UserCourse(
                1,
                10,
                15,
                new Timestamp(System.currentTimeMillis()),
                100
        );
    }

    public static TopicCourse createTestTopicCourse() {
        return new TopicCourse(1, 15);
    }

    public static TeacherCourse createTestTeacherCourse() {
        return new TeacherCourse(1, 10, 15);
    }

    public static UserDTO createUserTestDTO() {
        return UserService.getUserDTO(createTestUser());
    }

    public static TopicDTO createTopicTestDTO() {
        return TopicService.getTopicDTO(createTestTopic());
    }

    public static FullCourseDTO createFullCourseTestDTO() {
        Course course = createTestCourse();
        User user = createTestUser();
        Topic topic = createTestTopic();
        return new FullCourseDTO(
                course.getC_id(),
                topic.getT_id(),
                user.getU_id(),
                course.getName(),
                course.getDescription(),
                course.getStart_date(),
                course.getEnd_date(),
                course.getDuration(),
                1,
                Collections.singletonList(createTopicTestDTO()),
                topic.getName(),
                topic.getDescription(),
                Collections.singletonList(createUserTestDTO()),
                user.getFirst_name() + " " + user.getLast_name()
        );
    }

    public static UserCourseDTO createUserCourseTestDTO() {
        UserCourse uc = createTestUserCourse();
        return new UserCourseDTO(
                createUserTestDTO(),
                uc.getU_c_id(),
                uc.getU_id(),
                uc.getC_id(),
                uc.getRegistration_date(),
                uc.getFinal_mark());
    }
}
