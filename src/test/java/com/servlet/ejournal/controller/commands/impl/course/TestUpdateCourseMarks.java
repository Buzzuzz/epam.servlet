package com.servlet.ejournal.controller.commands.impl.course;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.services.CourseService;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserCourseDTO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;

class TestUpdateCourseMarks {
    private static final UpdateCourseMarks command = new UpdateCourseMarks();
    private HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private ApplicationContext contextMock = mock(ApplicationContext.class);
    private CourseService courseServiceMock = mock(CourseService.class);
    private UserService userServiceMock = mock(UserService.class);

    @BeforeEach
    void setup() {
        List<UserCourseDTO> ucDto = new ArrayList<>();
        ucDto.add(createUserCourseTestDTO());
        ucDto.add(createUserCourseTestDTO());

        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getParameter(anyString())).thenReturn("1");
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);

        when(contextMock.getCourseService()).thenReturn(courseServiceMock);
        when(contextMock.getUserService()).thenReturn(userServiceMock);

        when(userServiceMock.getEnrolledStudents(anyLong())).thenReturn(ucDto);
    }

    @Test
    void testSuccessfulUpdateCourseMark() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=marks&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingNumber() {
        when(reqMock.getParameter(COURSE_ID)).thenReturn("NaN");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testCantUpdateOneOfMarks() {
        when(courseServiceMock.updateStudentMark(createTestUserCourse(), createTestUserCourse().getFinal_mark())).thenReturn(ValidationError.DB_ERROR);
        assertDoesNotThrow(() -> command.execute(reqMock));
    }
}
