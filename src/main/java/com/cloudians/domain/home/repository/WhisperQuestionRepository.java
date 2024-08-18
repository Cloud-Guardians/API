package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.WhisperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhisperQuestionRepository extends JpaRepository<WhisperQuestion, Long> {
}
