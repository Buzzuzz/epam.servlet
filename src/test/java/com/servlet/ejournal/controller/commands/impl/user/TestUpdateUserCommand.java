package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.model.entities.*;
import com.servlet.ejournal.services.UserService;
import com.servlet.ejournal.services.dto.UserDTO;
import com.servlet.ejournal.services.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class TestUpdateUserCommand {
    private User user;
    private UserDTO userDTO;
    private final UpdateUserCommand command = new UpdateUserCommand();
    private final HttpServletRequest reqMock = mock(HttpServletRequest.class);
    private final ApplicationContext contextMock = mock(ApplicationContext.class);
    private final UserService serviceMock = mock(UserService.class);

    @BeforeEach
    void setup() throws ServiceException {
        user = new User(
                10,
                "mail@mail.com",
                "123456",
                "name",
                "surname",
                "123456789",
                UserType.STUDENT,
                false,
                false
        );

        userDTO = UserServiceImpl.getInstance().getUserDTO(user);

        when(reqMock.getSession()).thenReturn(mock(HttpSession.class));
        when(reqMock.getSession().getAttribute(LOGGED_USER_ATTR)).thenReturn(user);

        when(reqMock.getServletContext()).thenReturn(mock(ServletContext.class));
        when(reqMock.getServletContext().getAttribute(APPLICATION_CONTEXT)).thenReturn(contextMock);

        when(reqMock.getParameter(FIRST_NAME)).thenReturn(user.getFirst_name());
        when(reqMock.getParameter(LAST_NAME)).thenReturn(user.getLast_name());
        when(reqMock.getParameter(PHONE_NUMBER)).thenReturn(user.getPhone());

        when(contextMock.getUserService()).thenReturn(serviceMock);
        when(serviceMock.updateUserData(userDTO, user.getPassword(), reqMock.getParameter(PASSWORD_ATTR),
                reqMock.getParameter(PASSWORD_REPEAT_ATTR))).thenReturn(ValidationError.NONE);
    }

    @Test
    void testSuccessfulUserUpdate() {
        assertDoesNotThrow(() -> command.execute(reqMock));
        assertEquals(PageConstants.CABINET_PAGE, command.execute(reqMock));
    }

    @Test
    void testServiceFailToUpdateUserData() throws ServiceException {
        when(serviceMock.updateUserData(userDTO, user.getPassword(),
                reqMock.getParameter(PASSWORD_ATTR),
                reqMock.getParameter(PASSWORD_REPEAT_ATTR)))
                .thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(reqMock));
    }
}
