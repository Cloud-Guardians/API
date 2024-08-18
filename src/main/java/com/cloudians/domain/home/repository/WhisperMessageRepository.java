package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WhisperMessageRepository extends JpaRepository<WhisperMessage, Long> {
    // 페이지네이션 필요
    List<WhisperMessage> findByUserOrderByTimestamp(User user);

    boolean existsByUserAndSenderAndTimestampBetween(User user, SenderType sender, LocalDateTime questionDateTime, LocalDateTime twentyFourHoursLater);
}
