package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.WhisperMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhisperMessageJpaRepository extends JpaRepository<WhisperMessage, Long> {
}
