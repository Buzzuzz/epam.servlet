package com.servlet.ejournal.controller.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestAppContextListener {
    private AppContextListener listener;
    private ServletContextEvent sceMock;
    private ServletContext contextMock;

    @BeforeEach
    void setup() {
        listener = new AppContextListener();
        sceMock = mock(ServletContextEvent.class);
        contextMock = mock(ServletContext.class);

        when(sceMock.getServletContext()).thenReturn(contextMock);
        when(contextMock.getContextPath()).thenReturn(CONTROLLER_ATTR);
    }

    @Test
    void testAppContextCreation() {
        listener.contextInitialized(sceMock);

        verify(sceMock, times(1)).getServletContext();
        verify(contextMock, times(1)).getContextPath();
        verify(contextMock, times(2)).setAttribute(any(), any());
    }

    @Test
    void testAppContextDestroyed() {
        listener.contextDestroyed(sceMock);

        verify(sceMock, times(2)).getServletContext();
        verify(contextMock, times(1)).removeAttribute(CONTROLLER_ATTR);
    }
}
