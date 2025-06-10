package com.CareBook.MediSched.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request-> request
                        .requestMatchers(
                                "/user/register",
                                "/user/login",
                                "/doctor/search",
                                "/appointment/available-slots",
                                "/review/get-all/**",
                                "/user/forgot-password",
                                "/doctor/**",
                                "/department/get-all",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/bot/chat"
                        ).permitAll()
                        .requestMatchers("/doctor/request").hasRole("USER")
                        .requestMatchers(
                                "/doctor/get-all-requests",
                                "/doctor/decide-request",
                                "/doctor/update/**",
                                "/department/createDepartment",
                                "/patient/delete/**",
                                "/getBy-doctor"
                                ).hasRole("ADMIN")
                        .requestMatchers("/patient/update/**").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers("/appointment/book").hasAnyRole("USER", "PATIENT")
                        .requestMatchers("/patient/get-all",
                                "/appointment/update/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/review/evaluate",
                                "/patient/me",
                                "/appointment/me"
                        ).hasRole("PATIENT")
                        .requestMatchers("/doctor/me").hasRole("DOCTOR")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager-> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }



}
