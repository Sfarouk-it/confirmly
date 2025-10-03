package com.confirmly.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final SellerDetailsService sellerDetailsService;

    public SecurityConfig(SellerDetailsService sellerDetailsService) {
        this.sellerDetailsService = sellerDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // disable CSRF for API (optional if using JWT)
            .authorizeHttpRequests()
            .requestMatchers("/api/authantification/**", "/metaHookController/**").permitAll() // allow signup & login
            .anyRequest().authenticated() // protect everything else
            .and()
            .formLogin(); // optional: default form login

        return http.build();
    }
}