package com.CareBook.MediSched.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        // ko cần role
                        .requestMatchers(
                                "/user/register",
                                "/user/login",
                                "/user/forgot-password",
                                "/doctor/search",
                                "/doctor/**",
                                "/department/get-all",
                                "/appointment/available-slots",
                                "/review/get-all/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/bot/chat"
                        ).permitAll()

                        // role USER
                        .requestMatchers("/doctor/request").hasRole("USER")

                        // role ADMIN
                        .requestMatchers(
                                "/doctor/get-all-requests",
                                "/doctor/decide-request",
                                "/doctor/update/**",
                                "/department/createDepartment",
                                "/patient/delete/**",
                                "/appointment/getBy-doctor"
                        ).hasRole("ADMIN")

                        // role PATIENT
                        .requestMatchers(
                                "/review/evaluate",
                                "/patient/me",
                                "/appointment/me"
                        ).hasRole("PATIENT")

                        // role DOCTOR
                        .requestMatchers("/doctor/me").hasRole("DOCTOR")

                        // chung role
                        .requestMatchers("/patient/update/**").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers("/appointment/book").hasAnyRole("USER", "PATIENT")
                        .requestMatchers(
                                "/patient/get-all",
                                "/appointment/update/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
