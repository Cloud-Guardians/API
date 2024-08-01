package com.cloudians.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class QuerydslConfig {
	@Autowired
	EntityManager entityManager;
	

	
	@Bean
	public JPAQueryFactory jpaqueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
