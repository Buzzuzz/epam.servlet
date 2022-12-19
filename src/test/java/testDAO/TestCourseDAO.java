package testDAO;

import model.dao.DAOException;
import model.dao.DataSource;
import model.dao.TestSetup;
import model.dao.impl.CourseDAO;
import model.entities.Course;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TestCourseDAO {
    static CourseDAO dao = CourseDAO.getInstance();
    static Connection con;
    long generatedId;

    @BeforeAll
    static void setup() {
        TestSetup.setup();
        con = DataSource.getConnection();
    }

    @AfterAll
    static void clean() {
        TestSetup.cleanup();
        DataSource.close(con);
    }

    @BeforeEach
    void init() {
        generatedId = dao.save(con, new Course(0, "test_course", "test_desc", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
    }

    @AfterEach
    void cleanup() {
        dao.delete(con, generatedId);
    }

    @Test
    void testSuccessfulGetCourse() {
        assertTrue(dao.get(con, generatedId).isPresent());
        assertEquals(dao.get(con, generatedId).get().getC_id(), generatedId);
    }

    @Test
    void testSuccessfulGetAllCourses() {
        long toDelete = dao.save(con, new Course(0, "name", "desc", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        assertTrue(dao.getAll(con).size() >= 2);
        assertEquals(dao.delete(con, toDelete), toDelete);
    }

    @Test
    void testSuccessfulUpdateCourse() {
        assertTrue(dao.get(con, generatedId).isPresent());
        Course course = dao.get(con, generatedId).get();
        course.setName("new_test_name");
        assertTrue(dao.update(con, course) > 0);
        assertEquals(course.getName(), dao.get(con, generatedId).get().getName());
    }

    @Test
    void testSuccessfulDeleteCourse() {
        long toDelete = dao.save(con, new Course(0, "t", "t", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        assertEquals(toDelete, dao.delete(con, toDelete));
    }

    @Test
    void testSuccessfulInsertCourse() {
        Course course = new Course(0, "insert", "insertion test", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        long id = dao.save(con, course);
        assertTrue(id > 0);
        assertTrue(dao.get(con, id).isPresent());
        assertEquals(dao.get(con, id).get().getName(), "insert");
        assertEquals(id, dao.delete(con, id));
    }

    @Test
    void testGetInvalidCourse() {
        assertFalse(dao.get(con, -1).isPresent());
    }

    @Test
    void testDeleteInvalidCourse() {
        assertThrows(DAOException.class, () -> dao.delete(con, -1));
    }

    @Test
    void testInsertInvalidCourse() {
        assertThrows(DAOException.class, () -> dao.save(con, dao.get(con, generatedId).get()));
    }
}
