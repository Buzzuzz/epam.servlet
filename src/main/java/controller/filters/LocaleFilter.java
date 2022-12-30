package controller.filters;

import constants.CommandNameConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Objects;

import static constants.AttributeConstants.*;

@WebFilter(filterName = "LocaleFilter", value = "/*", initParams = {@WebInitParam(name = LOCALE_ATTR, value = LOCALE_EN)})
@Log4j2
public class LocaleFilter implements Filter {
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (req.getSession().getAttribute(LOCALE_ATTR) == null) {
            log.info("locale filter init locale");
            req.getSession().setAttribute(LOCALE_ATTR, config.getInitParameter(LOCALE_ATTR));
        }
        if (Objects.equals(req.getParameter(COMMAND_ATTR), CommandNameConstants.CHANGE_LOCALE_COMMAND)) {
            log.info("locale filter change locale to " + req.getParameter(LOCALE_ATTR));
            req.getSession().setAttribute(LOCALE_ATTR, req.getParameter(LOCALE_ATTR));
        }
        chain.doFilter(req, resp);
    }
}
