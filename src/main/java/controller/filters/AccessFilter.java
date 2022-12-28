package controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import static constants.AttributeConstants.*;
import static constants.CommandNameConstants.*;
import static constants.PageConstants.*;

@Log4j2
@WebFilter(filterName = "AccessFilter", value = "/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //TODO: implement access filter
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (req.getParameter(COMMAND_ATTR) == null) {
            req.setAttribute(COMMAND_ATTR, ERROR_COMMAND);
        }
        if (req.getAttribute(COMMAND_ATTR) == SIGN_UP_COMMAND) {
            chain.doFilter(req, servletResponse);
        }
        if (req.getSession().getAttribute(LOGGED_USER_ATTR) == null) {
            req.setAttribute(COMMAND_ATTR, LOG_IN_COMMAND);
        }
        chain.doFilter(req, servletResponse);
    }
}
