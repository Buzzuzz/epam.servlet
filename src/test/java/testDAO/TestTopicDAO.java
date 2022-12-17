package testDAO;

import dao.DAOException;
import dao.TestSetup;
import dao.impl.TopicDAO;
import entities.Topic;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TestTopicDAO {
    static TopicDAO dao = TopicDAO.getInstance();
    static long generatedId;

    @BeforeAll
    static void setup() {
        TestSetup.setup();
    }

    @AfterAll
    static void clean() {
        TestSetup.cleanup();
    }

    @BeforeEach
    void init() {
        generatedId = dao.save(new Topic(0, "new_topic", "desc"));
    }

    @AfterEach
    void cleanup() {
        dao.delete(generatedId);
    }

    @Test
    void testSuccessfulInsertTopic() {
        long returnId = dao.save(new Topic(0, "test_topic", "test_desc"));
        assertTrue(dao.get(returnId).isPresent());
        assertEquals("test_topic", dao.get(returnId).get().getName());
        assertEquals(returnId, dao.delete(returnId));
    }

    @Test
    void testSuccessfulGetTopic() {
        assertTrue(dao.get(generatedId).isPresent());
        assertEquals(generatedId, dao.get(generatedId).get().getT_id());
    }

    @Test
    void testSuccessfulGetAllTopics() {
        long resultId = dao.save(new Topic(0, "nme", "dsc"));
        assertTrue(dao.getAll().size() >= 2);
        dao.delete(resultId);
    }

    @Test
    void testSuccessfulDeleteTopic() {
        long deleteId = dao.save(new Topic(0, "delete", "delete"));
        assertEquals(deleteId, dao.delete(deleteId));
    }

    @Test
    void testSuccessfulUpdateTopic() {
        assertTrue(dao.get(generatedId).isPresent());
        Topic topic = dao.get(generatedId).get();
        topic.setName("updated");
        topic.setDescription("desc_updated");
        assertNotEquals(0, dao.update(topic));
        assertEquals(topic, dao.get(generatedId).get());
    }

    @Test
    void testInsertNotValidTopic() {
        assertThrows(DAOException.class, () -> dao.save(dao.get(generatedId).get()));
    }

    @Test
    void testGetNotValidTopic() {
        assertFalse(dao.get(-1).isPresent());
    }
}
