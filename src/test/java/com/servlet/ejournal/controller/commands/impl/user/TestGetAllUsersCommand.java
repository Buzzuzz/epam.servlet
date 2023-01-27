package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestGetAllUsersCommand {
    private static final GetAllUsersCommand command = new GetAllUsersCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);

    @BeforeAll
    static void setup() {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.getUserCount(any())).thenReturn(1);
        when(serviceMock.getAllUsers(any())).thenReturn(new ArrayList<>());
        when(serviceMock.getAllUserTypes()).thenReturn(new ArrayList<>());
    }

    @Test
    void testAnyParamsReturnUsersPage() {
        assertEquals(PageConstants.USERS_PAGE, command.execute(reqMock));
        verify(reqMock, times(7)).setAttribute(any(), any());
    }
}
