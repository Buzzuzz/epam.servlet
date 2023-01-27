package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

// TODO : find the cause why tests fail when run together
class TestDeleteUserCommand {
    private static final DeleteUserCommand command = new DeleteUserCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);

        when(reqMock.getParameter(USER_ID)).thenReturn("10");
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.deleteUser(10)).thenReturn(1L);
    }

    @Disabled
    @Test
    void testSuccessfulDeleteUser() {
        assertEquals("controller?command=get-all-users&", command.execute(reqMock));
    }

    @Disabled
    @Test
    void testNoValidUserId() {
        when(reqMock.getParameter(USER_ID)).thenReturn("invalidNumber");
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }

    @Test
    void testNoSuchUser() throws ServiceException {
        when(serviceMock.deleteUser(10)).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}