package com.cloudians.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration  // IoC 빈 (bean) 등록 
@EnableWebSecurity  // 필터 체인 관리 시작 어노테이
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}

	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable()
	            .authorizeRequests()
	                .antMatchers("/**").permitAll() // 인증 없이 접근 허용
	                .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
	            .and()
	            .cors(); // CORS 설정 추가
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
