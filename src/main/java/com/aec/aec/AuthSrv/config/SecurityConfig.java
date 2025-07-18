package com.aec.aec.AuthSrv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
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
          // 1) Habilitamos CORS (lee nuestro CorsConfigurationSource)
          .cors().and()

          // 2) Deshabilitamos CSRF (API REST stateless)
          .csrf().disable()

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
}

