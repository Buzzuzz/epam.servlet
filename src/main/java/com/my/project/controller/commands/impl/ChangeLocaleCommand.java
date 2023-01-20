package com.my.project.controller.commands.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ChangeLocaleCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        return (String) req.getSession().getAttribute(AttributeConstants.PREVIOUS_REQUEST);
    }
}
