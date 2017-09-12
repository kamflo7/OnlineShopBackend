package pl.kflorczyk.onlineshopbackend.jwt_authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        console_print(servletRequest);

        String authorization = servletRequest.getHeader("Authorization");
        if (authorization != null) {
            JwtAuthToken token = new JwtAuthToken(authorization.replaceAll("Bearer ", ""));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(request, response);
    }

    private void console_print(HttpServletRequest servletRequest) {
        String remoteHost = servletRequest.getRemoteHost();
        int remotePort = servletRequest.getRemotePort();

        String method = servletRequest.getMethod();
        String queryString = servletRequest.getQueryString();
        String requestURI = servletRequest.getRequestURI();
        String headerAuth = servletRequest.getHeader("Authorization");

        System.out.println(String.format("[INCOMING][RemoteHost:%s:%d][Method: %s][QueryString: %s][RequestURI: %s][AuthHeader: %s]",
                remoteHost, remotePort, method, queryString, requestURI, headerAuth));
    }

    @Override
    public void destroy() {

    }
}
