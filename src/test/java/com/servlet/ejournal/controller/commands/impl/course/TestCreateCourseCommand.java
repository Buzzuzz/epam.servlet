package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.services.CourseService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestCreateCourseCommand {
    private static final CreateCourseCommand command = new CreateCourseCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final CourseService serviceMock = mock(CourseService.class);

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));

        when(contextMock.getCourseService()).thenReturn(serviceMock);

        when(reqMock.getParameter(COURSE_END_DATE)).thenReturn("2023-01-01");
        when(reqMock.getParameter(COURSE_START_DATE)).thenReturn("2023-01-01");
        when(reqMock.getParameter(TOPIC_ID)).thenReturn("2");
        when(reqMock.getParameter(USER_ID)).thenReturn("1");
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
    }

    @Test
    void testSuccessfulCreateCourse() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-courses&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingID() {
        when(reqMock.getParameter(TOPIC_ID)).thenReturn("null");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testExceptionParsingDate() {
        when(reqMock.getParameter(COURSE_END_DATE)).thenReturn("date");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
