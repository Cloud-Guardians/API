package com.cloudians.domain.personaldiary.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;

@Repository
public interface PersonalDiaryEmotionRepository extends JpaRepository<PersonalDiaryEmotion, Long> {
    PersonalDiaryEmotion findPersonalDiaryEmotionByUserAndDate(User user, LocalDate date);
    Optional<PersonalDiaryEmotion> findByIdAndUser(Long emotionId, User user);
}