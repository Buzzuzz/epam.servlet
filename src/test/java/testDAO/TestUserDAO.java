package testDAO;

import exceptions.DAOException;
import model.dao.DataSource;
import model.dao.TestSetup;
import model.dao.impl.UserDAO;
import model.entities.User;
import model.entities.UserType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.List;

class TestUserDAO {
    static long generatedID;
    static UserDAO dao = UserDAO.getInstance();
    static User testUser;
    static Connection con;

    @BeforeAll
    static void setup() {
        TestSetup.setup();
        con = DataSource.getConnection();
    }

    @AfterAll
    static void cleanup() {
        TestSetup.cleanup();
        DataSource.close(con);
    }

    @BeforeEach
    void init() {
        testUser = new User(
                0,
                "test_email@gmail.com",
                "pass123",
                "John",
                "Doe",
                "+380972322160",
                UserType.STUDENT,
                false,
                false);
        generatedID = dao.save(con, testUser);
    }

    @AfterEach
    void clean() {
        dao.delete(con, generatedID);
    }

    @Test
    void testSuccessfulGetUser() {
        Assertions.assertEquals(dao.get(con, generatedID).get().getU_id(), generatedID);
    }

    @Test
    void testSuccessfulGetAllUsers() {
        long toRemove = dao.save(con, new User(0, "test", "p", "J", "D", "+38", UserType.STUDENT, false, false));
        List<User> temp = (List<User>) dao.getAll(con);
        Assertions.assertTrue(temp.size() >= 1);
        dao.delete(con, toRemove);
    }

    @Test
    void testSuccessfulUpdateUser() {
        testUser = dao.get(con, generatedID).get();
        testUser.setEmail("new_test_email@gmail.com");
        dao.update(con, testUser);
        Assertions.assertEquals(testUser.getEmail(), dao.get(con, generatedID).get().getEmail());
    }

    @Test
    void testSuccessfulDeleteUser() {
        long affected = dao.delete(con, generatedID);
        Assertions.assertEquals(affected, generatedID);
        Assertions.assertFalse(dao.get(con, generatedID).isPresent());
        generatedID = dao.save(con, testUser);
    }

    @Test
    void testSuccessfulInsertUser() {
        Assertions.assertEquals(dao.get(con, generatedID).get().getU_id(), generatedID);
        dao.delete(con, generatedID);
        generatedID = dao.save(con, testUser);
        Assertions.assertNotEquals(0, generatedID);
    }

    @Test
    void testGetNonExistentUser() {
        Assertions.assertFalse(dao.get(con, -1).isPresent());
    }

    @Test
    void testDeleteNonExistentUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.delete(con, -1));
//        Assertions.assertEquals(dao.delete(-1), 0);
    }

    @Test
    void testUpdateNonExistentUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.update(con, new User(-1)));
    }

    @Test
    void testInsertNotValidUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.save(con, new User(-1)));
    }
}
