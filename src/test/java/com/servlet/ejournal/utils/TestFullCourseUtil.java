package com.servlet.ejournal.utils;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.model.dao.impl.TeacherCourseDAO;
import com.servlet.ejournal.model.dao.impl.TopicCourseDAO;
import com.servlet.ejournal.model.dao.impl.TopicDAO;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.TeacherCourse;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.model.entities.TopicCourse;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.servlet.ejournal.constants.AttributeConstants.ENROLLED_ASC_SORTING;
import static com.servlet.ejournal.constants.AttributeConstants.ENROLLED_DESC_SORTING;
import static com.servlet.ejournal.utils.FullCourseUtil.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestFullCourseUtil {
    @Nested
    class TestGetCurrentTopic {
        private TopicDAO topicDAOMock;
        private TopicCourseDAO topicCourseDAOMock;
        private Connection conMock;
        private final Topic topic = createTestTopic();
        private final TopicCourse tc = createTestTopicCourse();
        private final long topicId = tc.getT_id();
        private final long courseId = tc.getC_id();

        @BeforeEach
        void setup() {
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
        private final User user = createTestUser();
        private final TeacherCourse tc = createTestTeacherCourse();
        private final long courseId = tc.getC_id();
        private final long teacherId = tc.getTch_id();

        @BeforeEach
        void setup() {
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
            courseList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                FullCourseDTO courseDTO = createFullCourseTestDTO();
                courseDTO.setEnrolled((int) ((Math.random() * (100 - 10)) + 10));
                courseList.add(courseDTO);
            }
            Collections.shuffle(courseList);
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
            assertEquals(5, result.size());
            long min = result.stream().min(Comparator.comparingLong(FullCourseDTO::getEnrolled)).get().getEnrolled();
            assertEquals(min, result.get(0).getEnrolled());
        }

        @Test
        void testSortByEnrolledStudentsDesc() {
            result = sortByEnrolledStudents(ENROLLED_DESC_SORTING, courseList);
            long max = result.stream().max(Comparator.comparingLong(FullCourseDTO::getEnrolled)).get().getEnrolled();
            assertEquals(5, result.size());
            assertEquals(max, result.get(0).getEnrolled());
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
