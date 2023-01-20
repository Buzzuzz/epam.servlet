package com.my.project.controller.filters;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import com.my.project.utils.RequestBuilder;

import java.io.IOException;
import java.util.Objects;

@WebFilter(filterName = "LocaleFilter", value = "/*", initParams = {@WebInitParam(name = AttributeConstants.LOCALE_ATTR, value = AttributeConstants.LOCALE_EN)})
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

        if (req.getSession().getAttribute(AttributeConstants.LOCALE_ATTR) == null) {
            log.debug("Locale filter init locale");
            req.getSession().setAttribute(AttributeConstants.LOCALE_ATTR, config.getInitParameter(AttributeConstants.LOCALE_ATTR));
        }
        if (Objects.equals(req.getParameter(AttributeConstants.COMMAND_ATTR), CommandNameConstants.CHANGE_LOCALE_COMMAND)) {
            log.debug("Locale filter change: " + req.getParameter(AttributeConstants.LOCALE_ATTR));
            req.getSession().setAttribute(AttributeConstants.LOCALE_ATTR, req.getParameter(AttributeConstants.LOCALE_ATTR));
        } else {
            req.getSession().setAttribute(
                    AttributeConstants.PREVIOUS_REQUEST,
                    RequestBuilder.buildRequest(
                            req.getServletPath(),
                            req.getParameterMap()));
        }

        chain.doFilter(req, resp);
    }
}
