package com.servlet.ejournal.controller.commands.impl.user;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.getSession().invalidate();
        return PageConstants.LOGIN_PAGE;
    }
}
