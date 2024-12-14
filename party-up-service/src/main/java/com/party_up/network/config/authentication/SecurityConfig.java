package com.party_up.network.config.authentication;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration class for setting up JWT-based authentication and
 * security rules in the application.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructor to inject JwtRequestFilter for filtering JWT tokens.
     *
     * @param jwtRequestFilter the JWT request filter
     */
    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Bean definition for PasswordEncoder using BCrypt hashing algorithm.
     *
     * @return an instance of BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating PasswordEncoder bean with BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain to manage access rules, session management,
     * and custom filters.
     *
     * @param http the HttpSecurity configuration
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain.");

        // Disable CSRF as we're using stateless JWT authentication
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                // Publicly accessible endpoints without authentication
                                .requestMatchers("/api/auth/login", "/api/create-user").permitAll()
                                .requestMatchers("/api/auth/logout").authenticated() // Protect logout endpoint
                                // All other endpoints require authentication
                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        session
                                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        // Stateless sessions due to JWT usage
                );

        // Add JwtRequestFilter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("JwtRequestFilter added to the security filter chain.");

        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean, required for authentication operations.
     *
     * @param authenticationConfiguration the configuration for authentication
     * @return the configured AuthenticationManager
     * @throws Exception in case of retrieval errors
     */
    @Bean
    public AuthenticationManager authentication(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        log.info("Retrieving AuthenticationManager from AuthenticationConfiguration.");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Allow cookies
            }
        };
    }

}
