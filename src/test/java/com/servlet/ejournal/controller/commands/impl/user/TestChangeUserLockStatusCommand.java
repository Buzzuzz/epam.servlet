package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestChangeUserLockStatusCommand {
    private static final ChangeUserLockStatusCommand command = new ChangeUserLockStatusCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);
        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);
        when(serviceMock.changeUserLockStatus(10, false)).thenReturn(1L);

        when(reqMock.getParameter(USER_ID)).thenReturn("10");
        when(reqMock.getParameter(USER_STATUS)).thenReturn("false");
    }

    @Test
    void testSuccessfulStatusChange() throws ServiceException {
        when(serviceMock.changeUserLockStatus(anyLong(), anyBoolean())).thenReturn(1L);
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals("controller?command=get-all-users&", command.execute(reqMock));
    }

    @Test
    void testInvalidUserId() {
        when(reqMock.getParameter(USER_ID)).thenReturn("invalidNumber");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testInvalidUserStatus() {
        when(reqMock.getParameter(USER_STATUS)).thenReturn("notABoolean");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testStatusChangeException() throws ServiceException {
        when(serviceMock.changeUserLockStatus(10, true)).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
