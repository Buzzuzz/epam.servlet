package com.servlet.ejournal.pools;

import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;

class TestCommandAccessPool {
    private static Pool<String, User> commands;
    private static User user;

    @BeforeAll
    static void setup() {
        commands = CommandAccessPool.getInstance();
        user = new User(
                1,
                null,
                null,
                null,
                null,
                null,
                UserType.STUDENT,
                false,
                false
        );
    }


    @Test
    void testGetAllowedInstancesUserIsNull() {
        Set<String> allowed = commands.getAllowedInstances(null);
        assertTrue(allowed.contains(LOG_OUT_COMMAND));
        assertTrue(allowed.contains(LOG_IN_COMMAND));
        assertTrue(allowed.contains(SIGN_UP_COMMAND));
        assertFalse(allowed.contains(UPDATE_USER_COMMAND));
    }

    @Test
    void testGetAllowedInstancesForStudent() {
        Set<String> allowed = commands.getAllowedInstances(user);
        assertTrue(allowed.contains(ENROLL_COMMAND));
        assertFalse(allowed.contains(LOG_IN_COMMAND));
    }

    @Test
    void testGetAllowedInstancesForTeacher() {
        user.setUser_type(UserType.TEACHER);
        Set<String> allowed = commands.getAllowedInstances(user);
        assertTrue(allowed.contains(UPDATE_COURSE_MARKS));
        assertFalse(allowed.contains(SIGN_UP_COMMAND));
    }

    @Test
    void testGetAllowedInstancesForAdmin() {
        user.setUser_type(UserType.ADMINISTRATOR);
        Set<String> allowed = commands.getAllowedInstances(user);
        assertTrue(allowed.contains(CREATE_USER));
        assertTrue(allowed.contains(CREATE_TOPIC));
        assertFalse(allowed.contains(LOG_IN_COMMAND));
    }

    @Test
    void testGetAllowedInstancesUserTypeIsNull() {
        user.setUser_type(null);
        assertThrows(NullPointerException.class, () -> commands.getAllowedInstances(user).contains(LOG_IN_COMMAND));
    }
}
