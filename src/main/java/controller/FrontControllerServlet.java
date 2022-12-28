package controller;

import controller.commands.Command;
import controller.commands.CommandException;
import controller.commands.CommandPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import static constants.AttributeConstants.*;
import static constants.PageConstants.*;


import java.io.IOException;

/**
 * Class-implementation of front controller pattern, extends {@link HttpServlet}.
 * Redirects user to some other part of program by executing {@link Command command}
 */
@WebServlet("/controller")
@Log4j2
public class FrontControllerServlet extends HttpServlet {
    //TODO: implement get | post methods

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + process(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + process(req));
    }

    private String process(HttpServletRequest req) {


        try {
            return CommandPool.getCommand(req.getParameter(COMMAND_ATTR)).execute(req);
        } catch (CommandException e) {
            log.error(e.getMessage(), e);
            return ERROR_PAGE;
        }
    }
}
