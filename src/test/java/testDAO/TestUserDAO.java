package testDAO;

import dao.impl.UserDAO;
import entities.User;
import entities.UserType;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

class TestUserDAO {
    static long generatedID;
    static UserDAO dao = new UserDAO();
    static User testUser = new User(0,
            "test_email@gmail.com",
            "pass123",
            "John",
            "Doe",
            "+380972322160",
            UserType.STUDENT,
            false,
            false);

    @AfterAll
    static void cleanup() {
        dao.delete(generatedID);
    }
    @Test
    void testSuccessfulGetUser() {
        generatedID = dao.save(testUser);
        Assertions.assertEquals(dao.get(generatedID).get().getU_id(), generatedID);
        dao.delete(generatedID);
    }

    @Test
    void testSuccessfulGetAllUsers() {
        List<User> temp = (List<User>) dao.getAll();
        Assertions.assertTrue(temp.size() > 1);
    }

    @Test
    void testSuccessfulUpdateUser() {
        generatedID = dao.save(testUser);
        testUser.setU_id(generatedID);
        testUser.setEmail("new_test_email@gmail.com");
        dao.update(testUser);
        Assertions.assertEquals(testUser.getEmail(), dao.get(generatedID).get().getEmail());
    }

    @Test
    void testSuccessfulDeleteUser() {
        long affected = dao.delete(generatedID);
        Assertions.assertTrue(affected > 0);
        Assertions.assertFalse(dao.get(generatedID).isPresent());
    }

    @Test
    void testSuccessfulInsertUser() {
        generatedID = dao.save(testUser);
        Assertions.assertNotEquals(0, generatedID);
    }
}
