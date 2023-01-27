package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;

class TestCreateUserCommand {
    private static final CreateUserCommand command = new CreateUserCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);
    private static final User user = createTestUser();
    private static final UserDTO userDTO = createUserTestDTO();

    @BeforeEach
    void setup() throws ServiceException {
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(any())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getServletPath()).thenReturn(CONTROLLER_ATTR);

        when(reqMock.getParameter(EMAIL_ATTR)).thenReturn(user.getEmail());
        when(reqMock.getParameter(PASSWORD_ATTR)).thenReturn(user.getPassword());
        when(reqMock.getParameter(PASSWORD_REPEAT_ATTR)).thenReturn(user.getPassword());
        when(reqMock.getParameter(FIRST_NAME)).thenReturn(user.getFirst_name());
        when(reqMock.getParameter(LAST_NAME)).thenReturn(user.getLast_name());
        when(reqMock.getParameter(PHONE_NUMBER)).thenReturn(user.getPhone());
        when(reqMock.getParameter(USER_TYPE_ATTR)).thenReturn(user.getUser_type().name());

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.createUser(userDTO,
                reqMock.getParameter(PASSWORD_ATTR),
                reqMock.getParameter(PASSWORD_REPEAT_ATTR),
                reqMock.getParameter(USER_TYPE_ATTR))).thenReturn(ValidationError.NONE);
    }

    @Test
    void testSuccessfulCreateUser() {
        assertEquals("controller?command=get-all-users&", command.execute(reqMock));
    }

    @Test
    void testFailedToCreateUser() throws ServiceException {
        doThrow(ServiceException.class).when(serviceMock).createUser(any(), anyString(), anyString(), anyString());
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
