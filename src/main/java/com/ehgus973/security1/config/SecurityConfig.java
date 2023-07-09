package com.ehgus973.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/user/**").authenticated();
            auth.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER");
            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.anyRequest().permitAll();
        });
        http.formLogin(form -> {
            form.loginPage("/loginForm");
            form.loginProcessingUrl("/login");
            form.defaultSuccessUrl("/");
        });
        http.oauth2Login(oauth -> oauth.loginPage("/loginForm"));
        return http.build();
    }
}
