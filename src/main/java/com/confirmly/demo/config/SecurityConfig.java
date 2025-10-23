package com.confirmly.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final SellerDetailsService sellerDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(SellerDetailsService sellerDetailsService , JwtRequestFilter jwtRequestFilter) {
        this.sellerDetailsService = sellerDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
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
            .cors() // ✅ هذا السطر يفعل إعداد CorsConfig الذي أنشأته
            .and()
            .csrf().disable() // JWT is stateless, CSRF not needed
            .authorizeHttpRequests()
            .requestMatchers(
                "/api/auth/**",
                "/metaHookController/**",
                "/api/platformsAuth/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no sessions

        // Add JWT filter before Spring Security filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}