package com.my.project.controller.filters;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.model.entities.User;
import com.my.project.services.impl.UserServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@WebFilter(filterName = "StatusFilter", value = "/*")
public class StatusFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        User currentUser = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
        if (currentUser != null && !Objects.equals(req.getParameter(AttributeConstants.COMMAND_ATTR), CommandNameConstants.LOG_OUT_COMMAND)) {
            Optional<User> daoUser = UserServiceImpl.getInstance().getUser(currentUser.getU_id());
            if (daoUser.isPresent() && daoUser.get().is_blocked()) {
                throw new ServletException("User is blocked!");
            }
        }
        chain.doFilter(request, response);
    }
}
