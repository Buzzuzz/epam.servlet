package com.servlet.ejournal.controller.filters;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestStatusFilter {
    private final StatusFilter filter = new StatusFilter();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final HttpServletResponse respMock = mock(HttpServletResponse.class);
    private final FilterChain chainMock = mock(FilterChain.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final UserService serviceMock = mock(UserService.class);
    private final User user = createTestUser();

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(CommandNameConstants.LOG_OUT_COMMAND);

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.getUser(user.getU_id())).thenReturn(Optional.of(user));
    }

    @Test
    void testUserIsNotBlocked() throws ServletException, IOException {
        filter.doFilter(reqMock, respMock, chainMock);
        verify(chainMock, times(1)).doFilter(reqMock, respMock);
    }

    @Test
    void testUserIsBlockedNoLogout() {
        user.set_blocked(true);
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(null);
        assertThrows(ServletException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }

    @Test
    void testUserIsBlockedLogOutCommand() {
        user.set_blocked(true);
        assertDoesNotThrow(() -> filter.doFilter(reqMock, respMock, chainMock));
    }

}
