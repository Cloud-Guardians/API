package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhisperMessageJpaRepository extends JpaRepository<WhisperMessage, Long> {
    List<WhisperMessage> findByUserAndSender(User user, SenderType sender);
}
