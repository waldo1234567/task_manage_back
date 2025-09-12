package com.task_management.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // EXACT origin(s) — do NOT use "*" if allowCredentials(true)
        config.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowCredentials(true);
        // allow the headers the client will send (Authorization is important)
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply to everything (including /ws/*)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
        }
}
