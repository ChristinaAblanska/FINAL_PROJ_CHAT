package com.example.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
private final AuthenticationProvider authenticationProvider;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        //DEFINE PUBLIC ENDPOINTS IF ANY
                        .requestMatchers("/public/**").permitAll()
                        //ALL THE REST AUTHENTICATION REQUIRED
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .logout(logout -> logout .logoutUrl("/logout") .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) .deleteCookies("JSESSIONID")
                        .clearAuthentication(true))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui-chat-docs.html"))
                .requestMatchers(AntPathRequestMatcher.antMatcher(" /chat-docs/**"));
    }

}