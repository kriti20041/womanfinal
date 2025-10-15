package com.ruralwomen.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for Postman testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // secure API endpoints
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> {}); // modern replacement for deprecated httpBasic()

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Only for testing with plain text passwords
        return NoOpPasswordEncoder.getInstance();
    }
}
