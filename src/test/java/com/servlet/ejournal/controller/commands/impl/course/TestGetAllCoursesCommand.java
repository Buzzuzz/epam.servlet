package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.utils.TestEntitiesUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestGetAllCoursesCommand {
    private static final GetAllCoursesCommand command = new GetAllCoursesCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final CourseService courseServiceMock = mock(CourseService.class);
    private final UserService userServiceMock = mock(UserService.class);
    private final TopicService topicServiceMock = mock(TopicService.class);
    private final User user = TestEntitiesUtil.createTestUser();

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);

        when(contextMock.getCourseService()).thenReturn(courseServiceMock);
        when(contextMock.getUserService()).thenReturn(userServiceMock);
        when(contextMock.getTopicService()).thenReturn(topicServiceMock);
    }

    @Test
    void testSuccessfulGetAllCourses() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("/pages/courses.jsp", command.execute(reqMock));
    }

    @Test
    void testApplicationContextNoCast() {
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(null);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
