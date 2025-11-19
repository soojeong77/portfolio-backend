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
                        // 2. 허용된 출처 목록에 로컬 테스트 주소와 실제 프론트엔드 주소 추가
                        .allowedOrigins(new String[]{
                                "https://portfolio-frontend-787n.onrender.com",
                                "https://[사용자님의 프론트엔드 주소].onrender.com", // <-- 프론트엔드 주소 확인 후 추가
                                "http://localhost:3000",
                                "http://localhost:5173" // React 등 프론트엔드 개발 환경 포트 추가
                        })
                        .allowedMethods(new String[]{"GET", "POST", "DELETE", "PUT"})
                        .allowCredentials(true);
            }
        };
    }
}