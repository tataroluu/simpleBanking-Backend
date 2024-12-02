package com.eteration.simplebanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/openapi/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .httpBasic();

        http.csrf().disable();
        http.headers().frameOptions().disable();

        return http.build();
    }
}
