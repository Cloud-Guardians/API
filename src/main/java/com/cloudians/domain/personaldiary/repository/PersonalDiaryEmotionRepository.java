package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalDiaryEmotionRepository extends JpaRepository<PersonalDiaryEmotion, Long> {
    Optional<PersonalDiaryEmotion> findByIdAndUser(Long emotionId, User user);
}