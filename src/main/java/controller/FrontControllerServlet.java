package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Class-implementation of front controller pattern, extends {@link HttpServlet}.
 * Redirects user to some other part of program by executing {@link controller.commands.Command command}
 */
@WebServlet("/")
public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //TODO: implement get | post methods
    }
}
