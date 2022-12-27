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
import static constants.PagesConstants.*;
import static constants.AttributesConstants.*;

@WebFilter(filterName = "AuthFilter", value = "/*")
@Log4j2
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        UserDAO dao = UserDAO.getInstance();
        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getSession().getAttribute(LOGGED_USER_ATTR) == null) {
            Connection con = DataSource.getConnection();

            Optional<User> daoResult = dao.getByEmail(con, request.getParameter(EMAIL_ATTR));
            DataSource.close(con);

            if (daoResult.isPresent()) {
                User user = daoResult.get();
                if (user.getPassword().equals(request.getParameter(PASSWORD_ATTR))) {
                    request.getSession().setAttribute(LOGGED_USER_ATTR, user);
                    log.info("User " + user.getEmail() + " logged in successful!");
                } else {
                    log.debug("Password doesn't match! " + user.getEmail());
                }
            } else {
                log.debug("No user with email " + request.getParameter(EMAIL_ATTR));
            }
        }

        chain.doFilter(request, resp);
    }
}
