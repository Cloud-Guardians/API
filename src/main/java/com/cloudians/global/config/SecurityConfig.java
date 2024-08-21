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
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
    
    @Bean
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilter(corsFilter)
            .authorizeRequests(authroize -> authroize
                .antMatchers("/oauth2/authorization/google").permitAll()
                .antMatchers("/auth/login/google").permitAll()
                .antMatchers("/auth/login/google/redirect").permitAll()
                .antMatchers("/api/auth/loginview").permitAll()
                .anyRequest().permitAll())
            .formLogin().disable()
            .httpBasic().disable()
            .oauth2Login() // OAuth2 로그인 설정
            .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
            .userInfoEndpoint()
            .userService(principalOauth2UserService); // 사용자 정보 서비스 설정

        return http.build();
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(http)), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JwtAuthorizationFilter(authenticationManager(http), userAuthRepository), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests(authroize -> authroize
                .antMatchers("/api/auth/signup").permitAll() // 회원가입 요청 허용
                .antMatchers("/api/auth/login").permitAll() // 로그인 요청 허용
                .antMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/api/admin/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()) // 나머지 요청은 허용
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }
}
