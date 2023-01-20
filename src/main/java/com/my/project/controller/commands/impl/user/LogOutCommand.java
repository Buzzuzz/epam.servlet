package com.my.project.controller.commands.impl.user;

import com.my.project.constants.PageConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
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
