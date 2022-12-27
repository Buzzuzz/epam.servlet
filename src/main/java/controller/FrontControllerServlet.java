package controller;

import commands.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

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
        if (req.getParameter("command") == null) {
            req.getRequestDispatcher(req.getContextPath() + "/index.jsp").forward(req, resp);
        }
        if (req.getSession().getAttribute("loggedUser") == null) {
            req.getRequestDispatcher(req.getContextPath() + "/pages/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("command") == null) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        if (req.getSession().getAttribute("loggedUser") == null) {
            req.getRequestDispatcher("pages/login.jsp").forward(req, resp);
        }
    }
}
