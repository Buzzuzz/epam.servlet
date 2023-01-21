package com.servlet.ejournal.controller.filters;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.pools.CommandAccessPool;
import com.servlet.ejournal.pools.PageAccessPool;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.model.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.AttributeConstants.*;

import java.io.IOException;

@Log4j2
@WebFilter(filterName = "AccessFilter", value = "/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        log.trace("Access filter start...");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        User user = (User) req.getSession().getAttribute(LOGGED_USER_ATTR);
        String[] commands = req.getParameterMap().get(COMMAND_ATTR);
        String userErrorString = user == null ? "anon user" : user.getEmail();
        String pageCause = String.format("Unauthorized access to page %s by %s", req.getServletPath(), userErrorString);

        if (PageAccessPool.getInstance().getAllowedInstances(user).contains(req.getServletPath())) {
            log.debug("Access to page granted");
            if (commands != null) {
                for (String command : commands) {
                    if (commands.length == 1 && commands[0].equals(CommandNameConstants.CHANGE_LOCALE_COMMAND)) {
                        chain.doFilter(req, servletResponse);
                        break;
                    } else {
                        if (CommandAccessPool.getInstance().getAllowedInstances(user).contains(command)) {
                            log.debug("Access to command granted");
                            chain.doFilter(req, servletResponse);
                        } else {
                            String commandCause = String.format("Unauthorized access to command %s by %s", command, userErrorString);
                            log.error(commandCause);
                            throw new CommandException(pageCause);
                        }
                    }
                }
            } else {
                log.debug("Command is null");
                chain.doFilter(req, servletResponse);
            }
        } else {
            log.error(pageCause);
            throw new CommandException(pageCause);
        }
    }
}
