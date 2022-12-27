package controller.commands.impl;

import controller.commands.Command;
import controller.commands.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public class SignUpCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        return null;
    }
}
