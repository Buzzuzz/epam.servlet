package com.servlet.ejournal.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;

class TestFrontControllerServlet {
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final HttpServletResponse respMock = mock(HttpServletResponse.class);
    private final FrontControllerTestChild testController = new FrontControllerTestChild();

    private static class FrontControllerTestChild extends FrontControllerServlet {
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            super.doGet(request, response);
        }

        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            super.doPost(request, response);
        }
    }

    @BeforeEach
    void setup() {
        when(reqMock.getParameter(any(String.class))).thenReturn(CHANGE_LOCALE_COMMAND);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(any(String.class))).thenReturn(PREVIOUS_REQUEST);
        when(reqMock.getRequestDispatcher(any(String.class))).thenReturn(mock(RequestDispatcher.class));
    }

    @Test
    void testDoGetNoException() {
        assertDoesNotThrow(() -> testController.doGet(reqMock, respMock));
    }

    @Test
    void testDoPostNoException() {
        assertDoesNotThrow(() -> testController.doPost(reqMock, respMock));
    }

    @Test
    void testDoGetServletException() {
        when(reqMock.getParameter(any(String.class))).thenReturn(null);
        assertThrows(ServletException.class, () -> testController.doGet(reqMock, respMock));
    }

    @Test
    void testDoPostServletException() {
        when(reqMock.getParameter(any(String.class))).thenReturn(null);
        assertThrows(ServletException.class, () -> testController.doPost(reqMock, respMock));
    }
}
