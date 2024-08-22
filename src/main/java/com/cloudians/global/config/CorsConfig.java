package com.cloudians.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {

	@Bean
	   public CorsFilter corsFilter() {
	      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      CorsConfiguration config = new CorsConfiguration();
	      config.setAllowCredentials(true); // 서버 응답 시 json을 자바 스크립트에서 처리할 수 있게 설정 
	      config.addAllowedOrigin("*"); // e.g. http://domain1.com 모든 ip 응답 허용
	      config.addAllowedHeader("*"); // 모든 header 응답 허용 
	      config.addAllowedMethod("*"); // 모든 get, post 등 허용

	      source.registerCorsConfiguration("/api/**", config);
	      return new CorsFilter(source);
	   }
}