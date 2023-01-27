package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;

class TestUpdateTopicCommand {
    private static final UpdateTopicCommand command = new UpdateTopicCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final TopicService serviceMock = mock(TopicServiceImpl.class);
    private static final TopicDTO topicDTO = createTopicTestDTO();

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(contextMock.getTopicService()).thenReturn(serviceMock);

        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
        when(reqMock.getParameter(TOPIC_ID)).thenReturn(String.valueOf(topicDTO.getTopicId()));
        when(reqMock.getParameter(TOPIC_NAME_ATTR)).thenReturn(topicDTO.getTopicName());
        when(reqMock.getParameter(TOPIC_DESCRIPTION_ATTR)).thenReturn(topicDTO.getTopicDescription());

        doReturn(1L).when(serviceMock).updateTopic(any());
    }

    @Test
    void testSuccessfulUpdateTopic() {
        assertEquals("controller?command=get-all-topics&", command.execute(reqMock));
    }

    @Test
    void testInvalidTopicId() {
        when(reqMock.getParameter(TOPIC_ID)).thenReturn("invalid");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testUpdateTopicException() throws ServiceException {
        when(serviceMock.updateTopic(topicDTO)).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
