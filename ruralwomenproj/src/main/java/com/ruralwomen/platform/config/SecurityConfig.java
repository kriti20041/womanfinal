package com.ruralwomen.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for API testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/uploads/**").permitAll() // allow your APIs
                        .anyRequest().permitAll() // everything else allowed
                )
                .formLogin(form -> form.disable()) // disable form login
                .httpBasic(basic -> basic.disable()); // disable basic auth

        return http.build();
    }
}
