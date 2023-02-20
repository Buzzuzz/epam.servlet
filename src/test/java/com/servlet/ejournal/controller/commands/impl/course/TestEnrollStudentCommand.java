package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.utils.TestEntitiesUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestEnrollStudentCommand {
    private static final EnrollStudentCommand command = new EnrollStudentCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final CourseService serviceMock = mock(CourseService.class);
    private final User user = TestEntitiesUtil.createTestUser();

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);

        when(contextMock.getCourseService()).thenReturn(serviceMock);

        when(reqMock.getParameter(COURSE_ID)).thenReturn("1");
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
    }

    @Test
    void testSuccessfulEnrollStudent() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=course-details&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingID() {
        when(reqMock.getParameter(COURSE_ID)).thenReturn("rick");
        assertThrows(CommandException.class, () -> command.execute(reqMock));

    }
}
