package com.servlet.ejournal.controller.commands.impl;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ChangeLocaleCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        return (String) req.getSession().getAttribute(AttributeConstants.PREVIOUS_REQUEST);
    }
}
