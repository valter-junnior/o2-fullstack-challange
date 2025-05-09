package com.o2.agent.config;

import com.o2.agent.utils.FileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FileLoader fileLoader() {
        return new FileLoader();
    }
}