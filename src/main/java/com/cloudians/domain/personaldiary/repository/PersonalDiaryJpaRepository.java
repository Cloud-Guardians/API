package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalDiaryJpaRepository extends JpaRepository<PersonalDiary, Long> {
    boolean existsPersonalDiaryByUserAndDate(User user, LocalDate date);

    Optional<PersonalDiary> findByUserAndDate(User user, LocalDate date);

    Optional<List<PersonalDiary>> findListByUser(User user);
    Optional<PersonalDiary> findByUserAndEmotionId(User user, Long emotionId);
    Optional<List<PersonalDiary>> findPersonalDiaryByUserAndDateBetweenOrderByDate(User user, LocalDate startDate, LocalDate endDate);
}
