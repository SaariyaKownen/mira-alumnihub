package com.alumni.portal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/register-student",
                    "/403",
                    "/error",
                    "/setup-admin"
                ).permitAll()
                // Static resources
                .requestMatchers(
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/*.css",
                    "/*.js",
                    "/photos/**",
                    "/galaxy-theme.css",
                    "/favicon.ico"
                ).permitAll()
                // Admin only
                .requestMatchers(
                    "/admin/control",
                    "/admin/control/**"
                ).hasRole("ADMIN")
                // Everything else needs login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/post-login", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/ai/**",
                    "/quiz/**",
                    "/profile/save-bio/**",
                    "/milestones/add",
                    "/messages/send",
                    "/messages/unread-count"
                )
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );
        return http.build();
    }
}