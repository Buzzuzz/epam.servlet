package controller.filters;

import constants.PageAccessPool;
import exceptions.CommandException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;

import java.io.IOException;

import static constants.AttributeConstants.*;

@Log4j2
@WebFilter(filterName = "AccessFilter", value = "/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //TODO: implement redirect to cabinet / login page instead of exception
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (PageAccessPool.getAllowedPages(req).contains(req.getServletPath())) {
            chain.doFilter(req, servletResponse);
        } else {
            User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
            String cause = "Unauthorized access to page " +
                    req.getServletPath() + " by "
                    + (user == null ? "anon user" : user.getEmail());
            log.error(cause);
            throw new CommandException(cause);

        }
    }
}
