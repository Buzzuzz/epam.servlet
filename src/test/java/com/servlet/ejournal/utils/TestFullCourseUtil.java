package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.model.dao.impl.TeacherCourseDAO;
import com.servlet.ejournal.model.dao.impl.TopicCourseDAO;
import com.servlet.ejournal.model.dao.impl.TopicDAO;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.utils.FullCourseUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static org.mockito.Mockito.*;

class TestFullCourseUtil {
    @Nested
    class TestGetCurrentTopic {
        private TopicDAO topicDAOMock;
        private TopicCourseDAO topicCourseDAOMock;
        private Connection conMock;
        private final long courseId = 5;
        private final long topicId = 10;
        private Topic topic;

        @BeforeEach
        void setup() {
            topic = new Topic(topicId, "name", "description");
            TopicCourse tc = new TopicCourse(topicId, courseId);

            conMock = mock(Connection.class);
            topicDAOMock = mock(TopicDAO.class);
            topicCourseDAOMock = mock(TopicCourseDAO.class);

            when(topicDAOMock.get(conMock, topicId)).thenReturn(Optional.of(topic));
            when(topicCourseDAOMock.get(conMock, courseId)).thenReturn(Optional.of(tc));
        }

        @Test
        void testConnectionIsNull() {
            assertThrows(DAOException.class, () -> getCurrentTopic(null, courseId, topicCourseDAOMock, topicDAOMock));
        }

        @Test
        void testCourseIdIsInvalid() {
            assertThrows(DAOException.class, () -> getCurrentTopic(conMock, -1, topicCourseDAOMock, topicDAOMock));
        }

        @Test
        void testSuccessfulGetCurrentTopic() {
            assertEquals(topic, getCurrentTopic(conMock, courseId, topicCourseDAOMock, topicDAOMock));
        }

        @Test
        void testNoSuchTopic() {
            when(topicDAOMock.get(conMock, topicId)).thenReturn(Optional.empty());
            assertThrows(DAOException.class, () -> getCurrentTopic(conMock, courseId, topicCourseDAOMock, topicDAOMock));
        }

        @Test
        void testNoSuchTopicCourse() {
            when(topicCourseDAOMock.get(conMock, courseId)).thenReturn(Optional.empty());
            assertThrows(DAOException.class, () -> getCurrentTopic(conMock, courseId, topicCourseDAOMock, topicDAOMock));
        }

    }

    @Nested
    class TestGetCurrentTeacher {
        private UserDAO userDAOMock;
        private TeacherCourseDAO teacherCourseDAOMock;
        private Connection conMock;
        private final long courseId = 1;
        private final long teacherId = 4;
        private User user;

        @BeforeEach
        void setup() {
            user = new User(
                    teacherId,
                    "mail@m.com",
                    "pass",
                    "name",
                    "surname",
                    "123456789",
                    UserType.TEACHER,
                    false,
                    false);

            TeacherCourse tc = new TeacherCourse(0, teacherId, courseId);

            userDAOMock = mock(UserDAO.class);
            teacherCourseDAOMock = mock(TeacherCourseDAO.class);
            conMock = mock(Connection.class);

            when(teacherCourseDAOMock.get(conMock, courseId)).thenReturn(Optional.of(tc));
            when(userDAOMock.get(conMock, teacherId)).thenReturn(Optional.of(user));
        }

        @Test
        void testConnectionIsNull() {
            assertThrows(DAOException.class, () -> getCurrentTeacher(null, courseId, teacherCourseDAOMock, userDAOMock));
        }

        @Test
        void testCourseIdInvalid() {
            assertThrows(DAOException.class, () -> getCurrentTeacher(conMock, -1, teacherCourseDAOMock, userDAOMock));
        }

        @Test
        void testSuccessfulGetCurrentTeacher() {
            assertEquals(user, getCurrentTeacher(conMock, courseId, teacherCourseDAOMock, userDAOMock));
        }

        @Test
        void testNoSuchCurrentTeacher() {
            when(userDAOMock.get(conMock, teacherId)).thenReturn(Optional.empty());
            assertThrows(DAOException.class, () -> getCurrentTeacher(conMock, courseId, teacherCourseDAOMock, userDAOMock));
        }

        @Test
        void testNoSuchTeacherCourse() {
            when(teacherCourseDAOMock.get(conMock, courseId)).thenReturn(Optional.empty());
            assertThrows(DAOException.class, () -> getCurrentTeacher(conMock, courseId, teacherCourseDAOMock, userDAOMock));
        }

    }

    @Nested
    class TestGetDuration {
        private Timestamp startDate;
        private Timestamp endDate;

        @BeforeEach
        void setup() {
            // Difference in time - 1 day
            // Duration - 2 days (started at this day, ended the next day)
            startDate = new Timestamp(86400 * 1000);
            endDate = new Timestamp(startDate.getTime() * 2);
        }

        @Test
        void testStartDateIsNull() {
            assertThrows(UtilException.class, () -> getDuration(null, endDate));
        }

        @Test
        void testEndDateIsNull() {
            assertThrows(UtilException.class, () -> getDuration(startDate, null));
        }

        @Test
        void testBothDatesIsNull() {
            assertThrows(UtilException.class, () -> getDuration(null, null));
        }

        @Test
        void testGetActualDuration() throws UtilException {
            assertEquals(2, getDuration(startDate, endDate));
        }

    }

    @Nested
    class TestSortByEnrolledStudents {
        private List<FullCourseDTO> courseList;
        private List<FullCourseDTO> result;

        @BeforeEach
        void setup() {
            // Ascending order - 2 : 1 : 3
            courseList = new ArrayList<>();

            courseList.add(new FullCourseDTO(
                    0,
                    0,
                    0,
                    "course in the middle",
                    "fifteen (15) students enrolled",
                    null,
                    null,
                    0,
                    10,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            courseList.add(new FullCourseDTO(
                    0,
                    0,
                    0,
                    "course one",
                    "ten (10) students enrolled",
                    null,
                    null,
                    0,
                    10,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            courseList.add(new FullCourseDTO(
                    0,
                    0,
                    0,
                    "course two",
                    "twenty (20) students enrolled",
                    null,
                    null,
                    0,
                    20,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }

        @Test
        void testNoSortingByEnrolledStudents() {
            result = sortByEnrolledStudents("wrongSortOption", courseList);
            assertEquals(result.get(0), courseList.get(0));
            assertEquals(result.get(1), courseList.get(1));
            assertEquals(result.get(2), courseList.get(2));
        }

        @Test
        void testSortByEnrolledStudentsAsc() {
            result = sortByEnrolledStudents(ENROLLED_ASC_SORTING, courseList);
            assertEquals(3, result.size());
            assertEquals(10, result.get(0).getEnrolled());
            assertEquals(result.get(1).getEnrolled(), courseList.get(0).getEnrolled());
        }

        @Test
        void testSortByEnrolledStudentsDesc() {
            result = sortByEnrolledStudents(ENROLLED_DESC_SORTING, courseList);
            assertEquals(3, result.size());
            assertEquals(20, result.get(0).getEnrolled());
            assertEquals(10, result.get(2).getEnrolled());
        }

        @Test
        void testSortByEnrolledStudentsSortingIsNull() {
            result = sortByEnrolledStudents(null, courseList);
            assertEquals(result, courseList);
        }

        @Test
        void testSortByEnrolledStudentsCourseListIsNull() {
            result = sortByEnrolledStudents("someSortOption", null);
            assertNotNull(result);
            assertEquals(0, result.size());
        }
    }
}
