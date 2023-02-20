package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.services.CourseService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestDeleteCourseCommand {
    private static final DeleteCourseCommand command = new DeleteCourseCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final CourseService serviceMock = mock(CourseService.class);

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);

        when(contextMock.getCourseService()).thenReturn(serviceMock);

        when(reqMock.getParameter(COURSE_ID)).thenReturn("1");
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
    }

    @Test
    void testSuccessfulDeleteCourse() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-courses&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingID() {
        when(reqMock.getParameter(COURSE_ID)).thenReturn("roll");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
