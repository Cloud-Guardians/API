package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalDiaryRepository {
    boolean existsPersonalDiaryByUserAndDate(User user, LocalDate date);

    Optional<PersonalDiary> findByUserAndDate(User user, LocalDate date);

    Optional<List<PersonalDiary>> findListByUser(User user);

    Optional<List<PersonalDiary>> findPersonalDiaryByUserAndDateBetweenOrderByDate(User user, LocalDate startOfMonth, LocalDate endOfMonth);

    PersonalDiary save(PersonalDiary personalDiary);

    void delete(PersonalDiary personalDiary);

    Optional<PersonalDiary> findById(Long id);
}