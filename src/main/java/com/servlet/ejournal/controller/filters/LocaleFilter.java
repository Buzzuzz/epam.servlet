package com.servlet.ejournal.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;
import static com.servlet.ejournal.utils.RequestBuilder.*;

@WebFilter(filterName = "LocaleFilter", value = "/*", initParams = {@WebInitParam(name = LOCALE_ATTR, value = LOCALE_EN)})
@Log4j2
@Getter
@Setter
public class LocaleFilter implements Filter {
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (!isLocalePresent(req)) {
            log.trace("Locale initialization...");
            resp.addCookie(new Cookie(LOCALE_ATTR, getInitLocale(req)));
        }
        if (Objects.equals(req.getParameter(COMMAND_ATTR), CHANGE_LOCALE_COMMAND)) {
            log.trace("Locale change: " + req.getParameter(LOCALE_ATTR));
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals(LOCALE_ATTR)) {
                    cookie.setValue(getLocaleForChange(req));
                    resp.addCookie(cookie);
                }
            }
        } else {
            log.trace("Build previous request...");
            req.getSession().setAttribute(PREVIOUS_REQUEST, buildRequest(req.getServletPath(), req.getParameterMap()));
        }

        chain.doFilter(req, resp);
    }

    private boolean isLocalePresent(HttpServletRequest req) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(LOCALE_ATTR))
                .map(Cookie::getValue).findAny().isPresent();
    }

    private String getInitLocale(HttpServletRequest req) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(LOCALE_ATTR))
                .map(Cookie::getValue).findAny()
                .orElse(config.getInitParameter(LOCALE_ATTR));
    }

    private String getLocaleForChange(HttpServletRequest req) {
        return req.getParameter(LOCALE_ATTR) == null ? getInitLocale(req) : req.getParameter(LOCALE_ATTR);
    }
}
