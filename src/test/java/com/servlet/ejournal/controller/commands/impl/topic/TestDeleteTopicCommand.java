package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.TopicService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestDeleteTopicCommand {
    private static final DeleteTopicCommand command = new DeleteTopicCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final TopicService serviceMock = mock(TopicService.class);

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(contextMock.getTopicService()).thenReturn(serviceMock);

        when(reqMock.getParameter(TOPIC_ID)).thenReturn("1");
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);

        doReturn(1L).when(serviceMock).deleteTopic(anyLong());
    }

    @Test
    void testSuccessfulDeleteTopic() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-topics&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingId() {
        when(reqMock.getParameter(TOPIC_ID)).thenReturn("invalidNumber");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testExceptionDeletingTopic() throws ServiceException {
        when(serviceMock.deleteTopic(1)).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
