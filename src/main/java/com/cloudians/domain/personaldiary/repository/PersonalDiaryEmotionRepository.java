package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDiaryEmotionRepository extends JpaRepository <PersonalDiaryEmotion, Long> {
}
