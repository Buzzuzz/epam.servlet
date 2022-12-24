package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * Class-implementation of front controller pattern, extends {@link HttpServlet}.
 * Redirects user to some other part of program by executing {@link controller.commands.Command command}
 */
@WebServlet("/controller")
@Log4j2
public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //TODO: implement get | post methods
        if (req.getParameter("command") == null) {
            req.getRequestDispatcher("pages/index.jsp").forward(req, resp);
        }

        log.debug(req.getSession());
        log.debug(req.getSession().getAttribute("loggedUser"));
    }
}
