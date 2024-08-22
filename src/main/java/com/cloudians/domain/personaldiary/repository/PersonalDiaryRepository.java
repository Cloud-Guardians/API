package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;

public interface PersonalDiaryRepository extends JpaRepository<PersonalDiary, Long> {
    boolean existsPersonalDiaryByUserAndDate(User user, LocalDate date);
    Optional<PersonalDiary> findByUserAndDate(User user, LocalDate date);
    Optional<PersonalDiary> findByIdAndUser(Long personalDiaryId, User user);
    Optional<List<PersonalDiary>> findListByUser(User user);
    Optional<List<PersonalDiary>> findPersonalDiaryByUserAndDateBetweenOrderByDate(User user, LocalDate startOfMonth, LocalDate endOfMonth);
 

    Optional<PersonalDiary> findByUserAndDate(User user, LocalDate date);

    Optional<List<PersonalDiary>> findPersonalDiaryByUserAndDateBetweenOrderByDate(User user, LocalDate startOfMonth, LocalDate endOfMonth);
}