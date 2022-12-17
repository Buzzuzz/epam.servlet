package testDAO;

import dao.DAOException;
import dao.impl.UserDAO;
import entities.User;
import entities.UserType;
import org.junit.jupiter.api.*;

import java.util.List;

class TestUserDAO {
    static long generatedID;
    static UserDAO dao = UserDAO.getInstance();
    static User testUser = new User(0, "test_email@gmail.com", "pass123", "John", "Doe", "+380972322160", UserType.STUDENT, false, false);

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
        long toRemove = dao.save(new User(0, "test", "p", "J", "D", "+38", UserType.STUDENT, false, false));
        List<User> temp = (List<User>) dao.getAll();
        Assertions.assertTrue(temp.size() >= 1);
        dao.delete(toRemove);
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

    @Test
    void testGetNonExistentUser() {
        Assertions.assertFalse(dao.get(-1).isPresent());
    }

    @Test
    void testDeleteNonExistentUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.delete(-1));
//        Assertions.assertEquals(dao.delete(-1), 0);
    }

    @Test
    void testUpdateNonExistentUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.update(new User(-1)));
    }

    @Test
    void testInsertNotValidUser() {
        Assertions.assertThrows(DAOException.class, () -> dao.save(new User(-1)));
    }
}
