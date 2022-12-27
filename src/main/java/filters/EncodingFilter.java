package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebFilter(filterName = "EncodingFilter", value = "/*")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        log.debug("Started encoding filter");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8;");
        resp.setCharacterEncoding("UTF-8");
        log.debug("Finished encoding filter");
        chain.doFilter(req, resp);
    }
}
