package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLogOutCommand {
    private static final LogOutCommand command = new LogOutCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final HttpSession sessionMock = mock(HttpSession.class);

    @BeforeAll
    static void setup() {
        when(reqMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    void testLogOutSuccessful() {
        assertEquals(PageConstants.LOGIN_PAGE, command.execute(reqMock));
        verify(reqMock, times(1)).getSession();
        verify(sessionMock, times(1)).invalidate();
    }
}
