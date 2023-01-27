package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.utils.TestEntitiesUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestCreateTopicCommand {
    private static final CreateTopicCommand command = new CreateTopicCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final TopicService serviceMock = mock(TopicService.class);
    private static final TopicDTO topicDTO = TestEntitiesUtil.createTopicTestDTO();

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(contextMock.getTopicService()).thenReturn(serviceMock);

        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);

        doReturn(topicDTO.getTopicId()).when(serviceMock).createTopic(any());
    }

    @Test
    void testSuccessfulTopicCreation() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-topics&", command.execute(reqMock));
    }

    @Test
    void testExceptionCreatingTopic() throws ServiceException {
        doThrow(ServiceException.class).when(serviceMock).createTopic(any());
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
