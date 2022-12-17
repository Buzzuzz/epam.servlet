package testDAO;

import dao.DAOException;
import dao.impl.CourseDAO;
import entities.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TestCourseDAO {
    static CourseDAO dao = CourseDAO.getInstance();
    long generatedId;

    @BeforeEach
    void init() {
        generatedId = dao.save(new Course(
                0,
                "test_course",
                "test_desc",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())));
    }

    @AfterEach
    void cleanup() {
        dao.delete(generatedId);
    }

    @Test
    void testSuccessfulGetCourse() {
        assertTrue(dao.get(generatedId).isPresent());
        assertEquals(dao.get(generatedId).get().getC_id(), generatedId);
    }

    @Test
    void testSuccessfulGetAllCourses() {
        long toDelete = dao.save(new Course(
                0,
                "name",
                "desc",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())));
        assertTrue(dao.getAll().size() >= 2);
        assertEquals(dao.delete(toDelete), toDelete);
    }

    @Test
    void testSuccessfulUpdateCourse() {
        assertTrue(dao.get(generatedId).isPresent());
        Course course = dao.get(generatedId).get();
        course.setName("new_test_name");
        assertTrue(dao.update(course) > 0);
        assertEquals(course.getName(), dao.get(generatedId).get().getName());
    }

    @Test
    void testSuccessfulDeleteCourse() {
        long toDelete = dao.save(new Course(0,
                "t",
                "t",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())));
        assertEquals(toDelete, dao.delete(toDelete));
    }

    @Test
    void testSuccessfulInsertCourse() {
        Course course = new Course(
                0,
                "insert",
                "insertion test",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );
        long id = dao.save(course);
        assertTrue(id > 0);
        assertTrue(dao.get(id).isPresent());
        assertEquals(dao.get(id).get().getName(), "insert");
        assertEquals(id, dao.delete(id));
    }

    @Test
    void testGetInvalidCourse() {
        assertFalse(dao.get(-1).isPresent());
    }

    @Test
    void testDeleteInvalidCourse() {
        assertThrows(DAOException.class, () -> dao.delete(-1));
    }

    @Test
    void testInsertInvalidCourse() {
        assertThrows(DAOException.class, () -> dao.save(dao.get(generatedId).get()));
    }
}
