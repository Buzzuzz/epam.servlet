package controller.filters;

import constants.CommandNameConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;
import exceptions.ServiceException;
import services.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Objects;

import static constants.AttributeConstants.*;

@WebFilter(filterName = "AuthFilter", value = "/*")
@Log4j2
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getSession().getAttribute(LOGGED_USER_ATTR) == null &&
                Objects.equals(request.getParameter(COMMAND_ATTR), CommandNameConstants.LOG_IN_COMMAND)) {
            try {
                User user = UserServiceImpl.getInstance().logIn(request.getParameter(EMAIL_ATTR), request.getParameter(PASSWORD_ATTR));
                request.getSession().setAttribute(LOGGED_USER_ATTR, user);
                request.getSession().setAttribute(USER_TYPE_ATTR, user.getUser_type());
                request.getSession().setAttribute(ERROR, null);
            } catch (ServiceException e) {
                log.error("Authorization failed, cause: " + e.getMessage());
                request.getSession().setAttribute(ERROR, e.getMessage());
            }
        }

        chain.doFilter(request, resp);
    }
}
