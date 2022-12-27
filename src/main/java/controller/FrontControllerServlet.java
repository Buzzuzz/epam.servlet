package controller;

import controller.commands.Command;
import controller.commands.CommandException;
import controller.commands.CommandPool;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import static constants.AttributesConstants.*;
import static constants.PagesConstants.*;


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
        resp.sendRedirect(req.getContextPath() + process(req, resp));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + process(req, resp));
    }

    private String process(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter(COMMAND_ATTR) == null) {
            return HOME_PAGE;
        }
        if (req.getSession().getAttribute(LOGGED_USER_ATTR) == null) {
            return LOGIN_PAGE;
        }

        try {
            return CommandPool.getCommand(req.getParameter(COMMAND_ATTR)).execute(req);
        } catch (CommandException e) {
            log.error(e.getMessage(), e);
//            Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
//            Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
            log.info(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            return ERROR_PAGE;
        }
    }
}
