package com.cloudians.global.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.PrincipalOauth2UserService;
import com.cloudians.global.filter.FirebaseAuthFilter;

import lombok.RequiredArgsConstructor;


@Configuration  // IoC 빈 (bean) 등록 
@EnableWebSecurity  // 필터 체인 관리 시작 어노테이
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final FirebaseAuthFilter firebaseAuthFilter;
	 private final CorsFilter corsFilter;
	    private final PrincipalOauth2UserService principalOauth2UserService;
	    private final UserAuthRepository userAuthRepository;
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}



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
	                		"/api/auth/login",
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
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable()
	            .authorizeRequests()
	                .antMatchers("/**").permitAll() // 인증 없이 접근 허용
	                .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
	            .and()
	            .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class)
	            .cors();// CORS 설정 추가
	    }
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
		 CorsConfiguration configuration = new CorsConfiguration();
		    configuration.addAllowedOriginPattern("*"); // 모든 출처를 허용
		    configuration.addAllowedMethod("*");
		    configuration.addAllowedHeader("*");
		    configuration.setAllowCredentials(true);
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", configuration);
		    return source;
	    }
}
