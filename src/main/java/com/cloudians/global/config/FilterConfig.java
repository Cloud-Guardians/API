package com.cloudians.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<MyFilter> filter1(){
		FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter());
		bean.addUrlPatterns("/*");
		bean.setOrder(0); // 낮은 번호가 필터에서 가장 먼저 실행됨
		return bean;
	}
	
}
