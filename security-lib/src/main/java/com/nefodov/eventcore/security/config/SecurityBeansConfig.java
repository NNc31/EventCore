package com.nefodov.eventcore.security.config;


import com.nefodov.eventcore.security.jwt.JwtAuthFilter;
import com.nefodov.eventcore.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(@Autowired JwtService jwtService) throws Exception {
        return new JwtAuthFilter(jwtService);
    }
}
