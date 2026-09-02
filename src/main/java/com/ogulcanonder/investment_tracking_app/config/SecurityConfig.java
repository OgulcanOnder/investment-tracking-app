package com.ogulcanonder.investment_tracking_app.config;

import com.ogulcanonder.investment_tracking_app.security.JwtAuthFilter;
import com.ogulcanonder.investment_tracking_app.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder,
                          JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasicAuth -> httpBasicAuth.disable());
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/v1/investment/**").authenticated()
                .requestMatchers("/api/v1/auth/logout").authenticated()
                .requestMatchers("/api/v1/auth/update-password").authenticated()
                .requestMatchers("/api/v1/auth/profile").authenticated()
                .requestMatchers("/api/v1/auth/forgot-password").permitAll()
                .requestMatchers("/api/v1/auth/reset-password").permitAll()
                .requestMatchers("/api/v1/instruments").permitAll()
                .requestMatchers("/api/v1/auth/register").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/auth/refresh").permitAll());
        http.sessionManagement(sessionManagement -> sessionManagement.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
