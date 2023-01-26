package com.servlet.ejournal.controller.commands.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class TestChangeLocaleCommand {
    private final ChangeLocaleCommand command = new ChangeLocaleCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);

    @BeforeEach
    void setup() {
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(any())).thenReturn(PREVIOUS_REQUEST);
    }
    @Test
    void testGetActualValue() {
        assertEquals(PREVIOUS_REQUEST, command.execute(reqMock));
    }

    @Test
    void testGetNullValue() {
        when(reqMock.getSession().getAttribute(any())).thenReturn(null);
        assertNull(command.execute(reqMock));
    }
}
