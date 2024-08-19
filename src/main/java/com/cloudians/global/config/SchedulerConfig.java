package com.cloudians.global.config;

import com.cloudians.domain.home.service.WhisperService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {

    private final WhisperService whisperService;

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void createWhisperQuestion() {
        log.info("스케쥴러 실행");

        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            log.info("유저에게 오늘의 질문 보내기: " + user.getUserEmail() + ", Date: " + today);
            whisperService.sendQuestion(user, today);
        }
    }
}