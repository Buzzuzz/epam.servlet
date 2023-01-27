package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.services.TopicService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestGetAllTopicsCommand {
    private static final GetAllTopicsCommand command = new GetAllTopicsCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final TopicService serviceMock = mock(TopicService.class);

    @BeforeAll
    static void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);
        when(contextMock.getTopicService()).thenReturn(serviceMock);
    }

    @Test
    void testGetAllTopicsSuccessful() {
        assertEquals(PageConstants.TOPICS_PAGE, command.execute(reqMock));
    }
}
