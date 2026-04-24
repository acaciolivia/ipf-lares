package com.ipfnoslares.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${nominatim.user-agent}")
    private String sNominatimUserAgent;

    /**
     * Bean do RestTemplate para consumo de APIs externas (ViaCEP e Nominatim).
     * O Nominatim exige um User-Agent identificando a aplicação.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder oBuilder) {
        return oBuilder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .defaultHeader("User-Agent", sNominatimUserAgent)
                .build();
    }
}
