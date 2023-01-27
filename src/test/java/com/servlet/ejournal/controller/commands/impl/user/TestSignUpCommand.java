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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.utils.TestEntitiesUtil.*;
import static com.servlet.ejournal.constants.PageConstants.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

class TestSignUpCommand {
    private static final SignUpCommand command = new SignUpCommand();
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private static final ApplicationContext contextMock = mock(ApplicationContext.class);
    private static final UserService serviceMock = mock(UserServiceImpl.class);
    private static final User user = createTestUser();
    private static final UserDTO userDTO = createUserTestDTO();

    @BeforeAll
    static void setup() throws ServiceException {
        user.setU_id(0);
        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(anyString())).thenReturn(contextMock);
        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));

        when(reqMock.getParameter(EMAIL_ATTR)).thenReturn(user.getEmail());
        when(reqMock.getParameter(FIRST_NAME)).thenReturn(user.getFirst_name());
        when(reqMock.getParameter(LAST_NAME)).thenReturn(user.getLast_name());
        when(reqMock.getParameter(PHONE_NUMBER)).thenReturn(user.getPhone());
        when(reqMock.getParameter(PASSWORD_REPEAT_ATTR)).thenReturn(user.getPassword());
        when(reqMock.getParameter(PASSWORD_ATTR)).thenReturn(user.getPassword());

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.signUp(userDTO, reqMock.getParameter(PASSWORD_ATTR), reqMock.getParameter(PASSWORD_REPEAT_ATTR))).thenReturn(ValidationError.NONE);
    }

    @Test
    void testSuccessfulSignUp() throws ServiceException {
        doReturn(ValidationError.NONE).when(serviceMock).signUp(any(), anyString(), anyString());
        assertEquals(LOGIN_PAGE, command.execute(reqMock));
    }

    @Test
    void testValidationErrorSignUp() throws ServiceException {
        doReturn(ValidationError.EMAIL).when(serviceMock).signUp(any(), anyString(), anyString());
        assertEquals(SIGN_UP_PAGE, command.execute(reqMock));
    }

    @Test
    void testSignUpException() throws ServiceException {
        doThrow(ServiceException.class).when(serviceMock).signUp(any(), anyString(), anyString());
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
