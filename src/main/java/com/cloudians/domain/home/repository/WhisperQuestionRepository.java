package com.cloudians.domain.home.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudians.domain.home.entity.WhisperQuestion;

public interface WhisperQuestionRepository extends JpaRepository<WhisperQuestion, Long> {
    Optional<WhisperQuestion> findByDate(LocalDate today);
}
