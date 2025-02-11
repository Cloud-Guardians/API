package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WhisperMessageJpaRepository extends JpaRepository<WhisperMessage, Long> {
    List<WhisperMessage> findByUserAndSender(User user, SenderType sender);
    long countByUserAndTimestamp(User user, Timestamp timestamp);
}
