package controller.commands.impl.user;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import exceptions.ServiceException;
import services.impl.UserServiceImpl;

import static constants.PageConstants.*;

@Log4j2
public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            UserServiceImpl.getInstance().signUp(req);
            return LOGIN_PAGE;
        } catch (ServiceException e) {
            return SIGN_UP_PAGE;
        }
    }
}
