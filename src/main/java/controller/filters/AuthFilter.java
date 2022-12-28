package controller.filters;

import constants.CommandNameConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;
import services.ServiceException;
import services.UserService;

import java.io.IOException;

import static constants.AttributeConstants.*;

@WebFilter(filterName = "AuthFilter", value = "/*")
@Log4j2
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getSession().getAttribute(LOGGED_USER_ATTR) == null
                && request.getParameter(COMMAND_ATTR) != null
                && request.getParameter(COMMAND_ATTR).equals(CommandNameConstants.LOG_IN_COMMAND)) {

            try {
                User user = UserService.logIn(request.getParameter(EMAIL_ATTR), request.getParameter(PASSWORD_ATTR));
                request.getSession().setAttribute(LOGGED_USER_ATTR, user);
            } catch (ServiceException e) {
                log.error(e.getMessage(), e);
            }

        }

        chain.doFilter(request, resp);
    }
}
