package com.servlet.ejournal.controller.filters;

import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;
import static com.servlet.ejournal.constants.PageConstants.*;

class TestAccessFilter {
    private final AccessFilter filter = new AccessFilter();
    private HttpServletRequest reqMock;
    private HttpServletResponse respMock;
    private FilterChain chainMock;
    private final Map<String, String[]> params = new HashMap<>();

    @BeforeEach
    void setup() {
        User user = new User(
                0,
                "test@user.com",
                null,
                null,
                null,
                null,
                UserType.STUDENT,
                false,
                false
        );

        reqMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        respMock = mock(HttpServletResponse.class);
        chainMock = mock(FilterChain.class);

        params.put(COMMAND_ATTR, new String[]{GET_ALL_COURSES_COMMAND});

        when(reqMock.getServletPath()).thenReturn(CONTROLLER_MAPPING);
        when(reqMock.getParameterMap()).thenReturn(params);
        when(reqMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute(LOGGED_USER_ATTR)).thenReturn(user);
    }

    @Test
    void testAccessToPageNoAccessToCommand() {
        params.put(COMMAND_ATTR, new String[]{CREATE_COURSE});
        assertThrows(CommandException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }

    @Test
    void testAccessToCommandNoAccessToPage() {
        params.clear();
        params.put(COMMAND_ATTR, new String[]{GET_ALL_COURSES_COMMAND, CHANGE_LOCALE_COMMAND});
        when(reqMock.getServletPath()).thenReturn(USERS_PAGE);
        assertThrows(CommandException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }

    @Test
    void testNoAccessToBoth() {
        params.clear();
        params.put(COMMAND_ATTR, new String[]{UPDATE_TOPIC, CHANGE_LOCALE_COMMAND});
        when(reqMock.getServletPath()).thenReturn(TOPICS_PAGE);
        assertThrows(CommandException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }

    @Test
    void testAccessToEverything() throws ServletException, IOException {
        filter.doFilter(reqMock, respMock, chainMock);
        verify(chainMock, times(1)).doFilter(reqMock, respMock);
    }


    @Test
    void testAccessToChangeLocaleOnly() throws ServletException, IOException {
        params.clear();
        params.put(COMMAND_ATTR, new String[]{CHANGE_LOCALE_COMMAND});
        filter.doFilter(reqMock, respMock, chainMock);
        verify(chainMock, times(1)).doFilter(reqMock, respMock);
    }

    @Test
    void testAccessToPageCommandIsNull() throws ServletException, IOException {
        params.clear();
        params.put(COMMAND_ATTR, null);
        filter.doFilter(reqMock, respMock, chainMock);
        verify(chainMock, times(1)).doFilter(reqMock, respMock);
    }

    @Test
    void testNoAccessToPageCommandIsNull() {
        params.clear();
        params.put(COMMAND_ATTR, null);
        when(reqMock.getServletPath()).thenReturn(TOPICS_PAGE);
        assertThrows(CommandException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }
}
