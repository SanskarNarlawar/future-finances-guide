package com.llmapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins (can be modified for production)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://*.vercel.app",
            "https://*.netlify.app",
            "https://*.appspot.com"
        ));
        
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // How long the browser can cache preflight request results
        configuration.setMaxAge(3600L);
        
        // Expose headers that the frontend can access
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}