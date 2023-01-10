package controller.commands.impl;

import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import static constants.AttributeConstants.COURSE_ID;
import static constants.PageConstants.COURSE_DETAILS_PAGE;

@Log4j2
public class CourseDetailsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(COURSE_ID, req.getParameter(COURSE_ID));
        return COURSE_DETAILS_PAGE;
    }
}
