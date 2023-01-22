package com.servlet.ejournal.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import lombok.Getter;
import lombok.Setter;

import static com.servlet.ejournal.constants.AttributeConstants.*;

import java.io.IOException;

@WebFilter(filterName = "EncodingFilter", value = "/*", initParams = {
        @WebInitParam(name = ENCODING_ATTR, value = ENCODING_UTF_8)
})
@Getter @Setter
public class EncodingFilter implements Filter {
    private FilterConfig config = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(config.getInitParameter(ENCODING_ATTR));
        resp.setContentType(String.format("text/html; charset=%s;", config.getInitParameter(ENCODING_ATTR)));
        resp.setCharacterEncoding(config.getInitParameter(ENCODING_ATTR));
        chain.doFilter(req, resp);
    }
}
