package com.my.project.controller.filters;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.PageAccessPool;
import com.my.project.exceptions.CommandException;
import com.my.project.model.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebFilter(filterName = "AccessFilter", value = "/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (PageAccessPool.getAllowedPages(req).contains(req.getServletPath())) {
            chain.doFilter(req, servletResponse);
        } else {
            User user = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
            String cause = "Unauthorized access to page " +
                    req.getServletPath() + " by "
                    + (user == null ? "anon user" : user.getEmail());
            log.error(cause);
            throw new CommandException(cause);

        }
    }
}
