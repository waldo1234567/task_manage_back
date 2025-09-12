package com.task_management.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsHeaderEnforcerFilter extends OncePerRequestFilter {
    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:5173",
            "http://127.0.0.1:5173"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            // echo the origin back (must be exact when credentials are used)
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            // allow headers that the client may send (Authorization, Content-Type, etc.)
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With, Accept");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Vary", "Origin");
        }

        // respond to preflight quickly
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
