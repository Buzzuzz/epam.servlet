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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestWithdrawStudentCommand {
    private static final WithdrawStudentCommand command = new WithdrawStudentCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final CourseService serviceMock = mock(CourseService.class);
    private static final User user = TestEntitiesUtil.createTestUser();

    @BeforeEach
    void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);
        when(contextMock.getCourseService()).thenReturn(serviceMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));

        when(reqMock.getSession().getAttribute(anyString())).thenReturn(user);
        when(reqMock.getParameter(COURSE_ID)).thenReturn(String.valueOf(user.getU_id()));
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
    }

    @Test
    void testSuccessfulWithdraw() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-courses&", command.execute(reqMock));
    }

    @Test
    void testExceptionParsingUserId() {
        when(reqMock.getParameter(COURSE_ID)).thenReturn("invalid");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
