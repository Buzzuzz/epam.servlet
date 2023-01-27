package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.servlet.ejournal.utils.TestEntitiesUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestLogInCommand {
    private static final LogInCommand command = new LogInCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);
    private static final HttpSession sessionMock = mock(HttpSession.class);
    private static final User user = createTestUser();

    @BeforeAll
    static void setup() throws ServiceException {
        when(reqMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute(LOGGED_USER_ATTR)).thenReturn(user);

        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(APPLICATION_CONTEXT)).thenReturn(contextMock);
        when(reqMock.getParameter(EMAIL_ATTR)).thenReturn(user.getEmail());
        when(reqMock.getParameter(PASSWORD_ATTR)).thenReturn(user.getPassword());

        when(contextMock.getUserService()).thenReturn(serviceMock);
        doReturn(user).when(serviceMock).logIn(anyString(), anyString());
    }

    @Test
    void testUserIsLoggedIn() {
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);
        assertEquals(PageConstants.CABINET_PAGE, command.execute(reqMock));
    }

    @Test
    void testUserIsLoggingIn() {
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(null);
        assertEquals(PageConstants.CABINET_PAGE, command.execute(reqMock));
    }

    @Test
    void testUserFailedLogIn() throws ServiceException {
        when(sessionMock.getAttribute(LOGGED_USER_ATTR)).thenReturn(null);
        doThrow(new ServiceException("test message")).when(serviceMock).logIn(anyString(), anyString());
        assertEquals(PageConstants.LOGIN_PAGE, command.execute(reqMock));
    }
}
