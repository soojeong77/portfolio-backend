package com.soojeong.portfolio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    public WebConfig() {
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                // 1. 매핑 경로를 모든 경로(/**)로 변경
                registry.addMapping("/**")
                        // 2. 허용된 출처 목록에 실제 프론트엔드 주소 추가
                        .allowedOrigins(new String[]{
                                "https://portfolio-backend-1-93tz.onrender.com", // ★★★ 이 주소를 추가하여 CORS 오류를 해결합니다 ★★★
                                "https://portfolio-frontend-787n.onrender.com",
                                "http://localhost:3000",
                                "http://localhost:5173" 
                        })
                        .allowedMethods(new String[]{"GET", "POST", "DELETE", "PUT"})
                        .allowCredentials(true);
            }
        };
    }
}