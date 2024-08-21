package com.cloudians.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final UserAuthRepository userAuthRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JwtAuthorizationFilter(authenticationManager, userAuthRepository), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests(authroize -> authroize
                .antMatchers("/oauth2/authorization/google",
                		"/oauth2/authorization/kakao",
                		"/auth/login/google",
                		"/auth/login/google/redirect",
                		"/api/auth/login",
                		"/api/auth/signup",
                		"/login/oauth2/code/google",
                		"/api/auth/kakao",
                		"/api/oauth/token",
                		"/auth/kakao/callback",
                		"/api/auth/kakao/callback").permitAll()
                .antMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated())
            .formLogin().disable()
            .httpBasic().disable()
            .oauth2Login()
            .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
            .userInfoEndpoint()
            .userService(principalOauth2UserService); // 사용자 정보 서비스 설정

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}
