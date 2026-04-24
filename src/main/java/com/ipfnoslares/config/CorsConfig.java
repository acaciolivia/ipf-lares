package com.ipfnoslares.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry oRegistry) {
        oRegistry.addMapping("/api/**")
                // Usa padrões para aceitar localhost (dev) e qualquer subdominio do Railway (prod).
                // allowedOriginPatterns (em vez de allowedOrigins) permite wildcards seguros.
                .allowedOriginPatterns(
                        "http://localhost:4200",
                        "https://*.up.railway.app",
                        "https://*.railway.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
