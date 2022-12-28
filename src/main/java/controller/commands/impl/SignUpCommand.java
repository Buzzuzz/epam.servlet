package controller.commands.impl;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import exceptions.ServiceException;
import services.UserService;
import static constants.PageConstants.*;

@Log4j2
public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            if (UserService.signup(req)) {
                return LOGIN_PAGE;
            }
            return SIGNIN_PAGE;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
    }
}
