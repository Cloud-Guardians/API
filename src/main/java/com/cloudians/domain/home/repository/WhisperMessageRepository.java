package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WhisperMessageRepository {

    List<WhisperMessage> findByUserAndSender(User user, SenderType sender);

    void save(WhisperMessage whisperMessage);

    boolean existsByUserAndSenderAndTimestampBetween(User user, SenderType sender, LocalDateTime questionDateTime, LocalDateTime twentyFourHoursLater);

    List<WhisperMessage> findByUserOrderByTimeStampDesc(User user, Long cursor, Long count);

    List<WhisperMessage> findBySearchKeywordOrderByTimeStampDesc(User user, Long cursor, Long count, String keyword);

    List<WhisperMessage> findByDateOrderByTimestampDesc(User user, Long cursor, Long count, LocalDate date);

    List<WhisperMessage> findListByUser(User user);
}
