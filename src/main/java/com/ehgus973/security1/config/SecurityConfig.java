package com.ehgus973.security1.config;

import com.ehgus973.security1.auth.PrincipalDetails;
import com.ehgus973.security1.auth.PrincipalDetailsService;
import com.ehgus973.security1.config.oauth2.PrincipalOauth2UserService;
import com.ehgus973.security1.filter.MyFilter;
import com.ehgus973.security1.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String loginId = authentication.getName();
            String password = (String) authentication.getCredentials();
            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(loginId);
            return new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
        }
    };
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService);
    }

    private final CorsConfig corsConfig;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalDetailsService principalDetailsService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter(), SecurityContextPersistenceFilter.class);
        http.addFilter(new JwtAuthenticationFilter(authenticationManager));
        http.csrf(c -> c.disable());
        http.cors(c -> c.disable());
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER", "MANAGER");
            auth.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER");
            auth.requestMatchers("/admin/**").hasAnyRole("ADMIN");
            auth.anyRequest().permitAll();
        });
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.addFilter(corsConfig.corsFilter());
        http.formLogin(fo -> fo.disable());
        http.httpBasic(ba -> ba.disable());
        return http.build();
    }
}
