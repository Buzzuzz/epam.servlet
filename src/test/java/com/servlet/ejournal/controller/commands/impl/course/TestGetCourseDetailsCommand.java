package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.model.entities.Course;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.utils.TestEntitiesUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestGetCourseDetailsCommand {
    private static final GetCourseDetailsCommand command = new GetCourseDetailsCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final CourseService serviceMock = mock(CourseService.class);
    private final User user = TestEntitiesUtil.createTestUser();
    private final Course course = TestEntitiesUtil.createTestCourse();

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);

        when(contextMock.getCourseService()).thenReturn(serviceMock);
        when(serviceMock.getCourse(course.getC_id())).thenReturn(Optional.of(course));
        when(serviceMock.getCourseDTO(course)).thenReturn(Optional.of(TestEntitiesUtil.createFullCourseTestDTO()));

        when(reqMock.getParameter(COURSE_ID)).thenReturn(String.valueOf(course.getC_id()));
    }

    @Test
    void testSuccessfulGetCourseDetails() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("/pages/courseDetailsPage.jsp", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingID() {
        when(reqMock.getParameter(COURSE_ID)).thenReturn("yay");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
