package app;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

class StatelessAuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    protected StatelessAuthenticationFilter(TokenAuthenticationService taService) {
        this.tokenAuthenticationService = taService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        Authentication auth = tokenAuthenticationService.getAuthentication((HttpServletRequest) req);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(req, res);
        } else {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
