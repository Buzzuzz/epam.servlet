package com.servlet.ejournal.controller.filters;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

// TODO : TestSetupClass to create test users, courses, topics and dtos
public class TestStatusFilter {
    private HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private HttpServletResponse respMock = mock(HttpServletResponse.class);
    private ApplicationContext contextMock;
    private UserService serviceMock;
    private User user;

    @BeforeEach
    void setup() {
    }

    @Test
    void testUserIsNotBlocked() {

    }

    @Test
    void testUserIsBlocked() {

    }

    @Test
    void testUserIsBlockedLogOutCommand() {

    }

}
