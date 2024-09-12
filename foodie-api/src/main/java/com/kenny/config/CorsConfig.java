package com.kenny.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {
    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");

        // allow to send cookie info
        configuration.setAllowCredentials(true);

        // allow all request methods
        configuration.addAllowedMethod("*");

        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(corsConfigurationSource);
    }
}