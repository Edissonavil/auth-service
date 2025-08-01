package com.aec.aec.AuthSrv.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.aec.aec.AuthSrv.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService uds) {
        this.userDetailsService = uds;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authManager,
                                           DaoAuthenticationProvider authProvider) throws Exception {
        http
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          // 2) Deshabilitamos CSRF (API REST stateless)
          .csrf(csrf -> csrf.disable())
          // 3) Configuramos las rutas públicas y preflight
          .authorizeHttpRequests(auth -> auth
              // Login y refresh sin token
              .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh").permitAll()
              // Preflight CORS para cualquier /api/auth/**
              .requestMatchers(HttpMethod.OPTIONS, "/api/auth/**").permitAll()
              // El resto requiere JWT
              .anyRequest().authenticated()
          )
          // 4) Stateless
          .sessionManagement(sm ->
              sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          // 5) Proveedor de autenticación (tu DaoAuthenticationProvider + filtro JWT)
          .authenticationProvider(authProvider);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
            "https://gateway-production-129e.up.railway.app",
            "https://aecf-production.up.railway.app",
            "https://file-service-production-31f3.up.railway.app",
            "https://aecblock.com"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}

