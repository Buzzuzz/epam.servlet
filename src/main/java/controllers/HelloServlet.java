package controllers;

import java.io.*;

import dao.UserDAO;
import entities.User;
import entities.UserType;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @Override
    public void init() {
        message = "Hello World!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        User u = new User(
                0,
                "ema@gmail.com",
                "passrd",
                "first_name",
                "last_name",
                "0978927",
                UserType.STUDENT,
                false,
                false
        );
        long id = UserDAO.createUser(u);
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + id + "</h1>");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
    }
}