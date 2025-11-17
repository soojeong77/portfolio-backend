package com.soojeong.portfolio; // 프로젝트 패키지명에 맞게

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("https://portfolio-frontend-787n.onrender.com") // 프론트 URL
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                        .allowCredentials(true);
            }
        };
    }
}
