package com.servlet.ejournal.pools;

import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.servlet.ejournal.constants.PageConstants.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.*;

class TestPageAccessPool {
    private static Pool<String, User> pages;
    private static User user;

    @BeforeAll
    static void setup() {
        pages = PageAccessPool.getInstance();
        user = createTestUser();
    }

    @Test
    void testGetAllowedPagesUserIsNull() {
        Set<String> allowed = pages.getAllowedInstances(null);
        assertTrue(allowed.contains(LOGIN_PAGE));
        assertTrue(allowed.contains(RESTORE_PASSWORD_PAGE));
        assertFalse(allowed.contains(CABINET_PAGE));
    }

    @Test
    void testGetAllowedPagesForStudent() {
        Set<String> allowed = pages.getAllowedInstances(user);
        assertTrue(allowed.contains(CABINET_PAGE));
        assertTrue(allowed.contains(COURSES_PAGE));
        assertFalse(allowed.contains(SIGN_UP_PAGE));
    }

    @Test
    void testGetAllowedPagesForTeacher() {
        user.setUser_type(UserType.TEACHER);
        Set<String> allowed = pages.getAllowedInstances(user);
        assertTrue(allowed.contains(MARKS_PAGE));
        assertFalse(allowed.contains(LOGIN_PAGE));
    }

    @Test
    void testGetAllowedPagesForAdmin() {
        user.setUser_type(UserType.ADMINISTRATOR);
        Set<String> allowed = pages.getAllowedInstances(user);
        assertTrue(allowed.contains(USERS_PAGE));
        assertFalse(allowed.contains(SIGN_UP_PAGE));
    }

    @Test
    void testGetAllowedPagesUserTypeIsNull() {
        user.setUser_type(null);
        assertThrows(NullPointerException.class, () -> pages.getAllowedInstances(user));
    }
}
