package todo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*.html")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        String uri = req.getRequestURI();
        if (uri.endsWith("login.html") || uri.endsWith("reg.html")) {
            chain.doFilter(sreq, sresp);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect("login.html");
            return;
        }
        chain.doFilter(sreq, sresp);
    }
}