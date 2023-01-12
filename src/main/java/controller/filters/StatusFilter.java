package controller.filters;

import constants.CommandNameConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.impl.UserDAO;
import model.entities.User;
import services.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static constants.AttributeConstants.*;

@WebFilter(filterName = "StatusFilter", value = "/*")
public class StatusFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        User currentUser = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        if (currentUser != null && !Objects.equals(req.getParameter(COMMAND_ATTR), CommandNameConstants.LOG_OUT_COMMAND)) {
            Optional<User> daoUser = UserServiceImpl.getInstance().getUser(currentUser.getU_id());
            if (daoUser.isPresent() && daoUser.get().is_blocked()) {
                throw new ServletException("User is blocked!");
            }
        }
        chain.doFilter(request, response);
    }
}
