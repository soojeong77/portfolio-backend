package com.soojeong.portfolio;

import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.boot.SpringApplication; 

// ⚠️ PasswordEncoder 관련 import는 이제 SecurityConfig에만 남겨두고 여기서 제거합니다.
// import org.springframework.context.annotation.Bean; 
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder; 

@SpringBootApplication
public class PortfolioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioBackendApplication.class, args);
    }
    
    // 👈 이 부분에 더 이상 @Bean public PasswordEncoder... 메서드가 없어야 합니다.
}