package com.cloudians.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.cloudians.domain.home.service.WhisperService;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
	// taskScheduler : 스케줄링 작업인터페이스, 스케줄링된 작업을 실행할 타이밍 결정
	System.out.println("taskScheduler 작동 시작");
	// ThreadPoolTaskScheduler :taskScheduler 인터페이스 구현체
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 풀 크기설정, 최대 10개의 스레드가 "동시에" 일어나 스케줄 처리 가능
        scheduler.setPoolSize(10);
        // 스케줄러 초기화
        scheduler.initialize();
        return scheduler;
    }
    
}