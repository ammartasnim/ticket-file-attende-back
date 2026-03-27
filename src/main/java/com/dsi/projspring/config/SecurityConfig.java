package com.dsi.projspring.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(org.springframework.security.config.Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agencies/**", "/services/**").permitAll()

                        .requestMatchers("/tickets/generate").hasRole("CLIENT")
                        .requestMatchers("/tickets/waitTime/**", "/tickets/position/**").hasRole("CLIENT")
                        .requestMatchers("/tickets/cancel/**").hasAnyRole("CLIENT", "AGENT", "ADMIN")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/agencies/**", "/services/**", "/counters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/agencies/**", "/counters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/agencies/**", "/services/**", "/counters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/agencies/toggle/**").hasRole("ADMIN")

                        .requestMatchers("/agent/**").hasRole("AGENT")
                        .requestMatchers("/tickets/next/**").hasRole("AGENT")
                        .requestMatchers("/tickets/agency/**").hasRole("AGENT")
                        .requestMatchers(HttpMethod.PATCH, "/counters/toggle/**").hasAnyRole("AGENT")

                        .requestMatchers(HttpMethod.GET, "/counters/**").hasAnyRole("AGENT", "ADMIN")
                        .requestMatchers("/tickets/**").authenticated()

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:8100", "http://localhost:8101"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
