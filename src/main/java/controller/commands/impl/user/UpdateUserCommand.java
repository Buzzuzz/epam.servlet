package controller.commands.impl.user;

import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ErrorType;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.User;
import services.UserService;
import services.dto.UserDTO;
import services.impl.UserServiceImpl;

import static constants.AttributeConstants.*;
import static exceptions.ErrorType.NONE;

public class UpdateUserCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserService service = UserServiceImpl.getInstance();

            User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            UserDTO userDTO = new UserDTO(
                    user.getU_id(),
                    user.getEmail(),
                    req.getParameter(FIRST_NAME),
                    req.getParameter(LAST_NAME),
                    req.getParameter(PHONE_NUMBER),
                    user.getUser_type().name(),
                    String.valueOf(user.is_blocked()),
                    String.valueOf(user.isSend_notification())
            );

            ErrorType error = service.updateUserData(
                    userDTO,
                    user.getPassword(),
                    req.getParameter(PASSWORD_ATTR),
                    req.getParameter(PASSWORD_REPEAT_ATTR));

            req.getSession().setAttribute(ERROR, error);

            if (error.equals(NONE)) {
                req.getSession().setAttribute(LOGGED_USER_ATTR, service.getUser(user.getU_id()).get());
            }
            return PageConstants.CABINET_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
