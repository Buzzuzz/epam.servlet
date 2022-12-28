package testDAO;

import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.TestSetup;
import model.dao.impl.TopicDAO;
import model.entities.Topic;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class TestTopicDAO {
    static TopicDAO dao = TopicDAO.getInstance();
    static long generatedId;
    static Connection con;

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
        generatedId = dao.save(con, new Topic(0, "new_topic", "desc"));
    }

    @AfterEach
    void cleanup() {
        dao.delete(con, generatedId);
    }


    @Test
    void testSuccessfulInsertTopic() {
        long returnId = dao.save(con, new Topic(0, "test_topic", "test_desc"));
        assertEquals("test_topic", dao.get(con, returnId).get().getName());
        assertEquals(returnId, dao.delete(con, returnId));
    }

    @Test
    void testSuccessfulGetTopic() {
        assertTrue(dao.get(con, generatedId).isPresent());
        assertEquals(generatedId, dao.get(con, generatedId).get().getT_id());
    }

    @Test
    void testSuccessfulGetAllTopics() {
        long resultId = dao.save(con, new Topic(0, "nme", "dsc"));
        assertTrue(dao.getAll(con).size() >= 2);
        dao.delete(con, resultId);
    }

    @Test
    void testSuccessfulDeleteTopic() {
        long deleteId = dao.save(con, new Topic(0, "delete", "delete"));
        assertEquals(deleteId, dao.delete(con, deleteId));
    }

    @Test
    void testSuccessfulUpdateTopic() {
        assertTrue(dao.get(con, generatedId).isPresent());
        Topic topic = dao.get(con, generatedId).get();
        topic.setName("updated");
        topic.setDescription("desc_updated");
        assertNotEquals(0, dao.update(con, topic));
        assertEquals(topic, dao.get(con, generatedId).get());
    }

    @Test
    void testInsertNotValidTopic() {
        assertThrows(DAOException.class, () -> dao.save(con, dao.get(con, generatedId).get()));
    }

    @Test
    void testGetNotValidTopic() {
        assertFalse(dao.get(con, -1).isPresent());
    }
}
