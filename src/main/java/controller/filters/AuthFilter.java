package controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.dao.DataSource;
import model.dao.impl.UserDAO;
import model.entities.User;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebFilter(filterName = "AuthFilter", value = "/*")
@Log4j2
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        UserDAO dao = UserDAO.getInstance();
        HttpServletRequest request = (HttpServletRequest) req;

        log.debug("Started Auth Filter");

        if (request.getSession().getAttribute("loggedUser") == null) {
            Connection con = DataSource.getConnection();

            Optional<User> daoResult = dao.getByEmail(con, request.getParameter("email"));
            DataSource.close(con);

            if (daoResult.isPresent()) {
                User user = daoResult.get();
                if (user.getPassword().equals(request.getParameter("password"))) {
                    request.getSession().setAttribute("loggedUser", user);
                    log.info("User " + user.getEmail() + " logged in successful!");
                } else {
                    log.debug("Password doesn't match!");
                }
            } else {
                log.debug("No user with email " + request.getParameter("email"));
            }
        } else {
            log.debug("User already is logged in!");
        }

        log.debug("Finished AuthFilter");


        chain.doFilter(request, resp);
    }
}
