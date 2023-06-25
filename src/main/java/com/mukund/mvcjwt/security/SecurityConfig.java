package com.mukund.mvcjwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        PasswordEncoder getEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationProvider getAuthenticationProvider(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
                daoAuthenticationProvider.setUserDetailsService(userDetailsService);
                return daoAuthenticationProvider;
        }

        @Bean
        AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        SecurityFilterChain SecurityFilterChain(HttpSecurity http, JWTAuthenticationFilter jwtAuthenticationFilter,
                        JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                        AuthenticationProvider authenticationProvider)
                        throws Exception {
                return http.csrf(csrfConfig -> csrfConfig.disable())
                                .cors(corsConfig -> corsConfig.disable())
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
                                                .httpStrictTransportSecurity(
                                                                hstsConfig -> hstsConfig.includeSubDomains(true)
                                                                                .maxAgeInSeconds(315536000))
                                                .xssProtection(
                                                                xssProtectionConfig -> xssProtectionConfig.headerValue(
                                                                                HeaderValue.ENABLED_MODE_BLOCK))
                                                .contentSecurityPolicy(
                                                                contentSecurityPolicy -> contentSecurityPolicy
                                                                                .policyDirectives(
                                                                                                "default-src 'self';")))
                                .authorizeHttpRequests(
                                                authConfig -> authConfig.requestMatchers("/api/**").permitAll()
                                                                .anyRequest().authenticated())
                                .sessionManagement(
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                                .addFilterBefore(jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

}