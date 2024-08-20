package com.cloudians.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 상태 없는 세션 관리
            .and()
            .addFilter(corsFilter) // CORS 필터 추가
            .addFilterBefore(new JwtAuthenticationFilter(authManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JwtAuthorizationFilter(authManager, userAuthRepository), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests(authroize -> authroize
                .antMatchers("/api/join").permitAll() // 회원가입 요청 허용
            	.antMatchers("/api/login").permitAll() // 로그인 요청 허용
            	// .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN") // "ROLE_" 접두사 없이 사용
                .antMatchers("/api/admin/**").hasAuthority("ADMIN") // "ROLE_" 접두사 없이 사용
                .anyRequest().permitAll()) // 나머지 요청은 허용
            .formLogin().disable() // 폼 로그인 비활성화
            .httpBasic().disable(); // HTTP Basic 인증 비활성화
           // .oauth2Login() // OAuth2 로그인 설정
           // .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
           // .userInfoEndpoint()
          //  .userService(principalOauth2UserService); // 사용자 정보 서비스 설정

        return http.build();
    }
}
