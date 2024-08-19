package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.WhisperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WhisperQuestionRepository extends JpaRepository<WhisperQuestion, Long> {
    Optional<WhisperQuestion> findByDate(LocalDate today);
}
