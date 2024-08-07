package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonalDiaryRepository extends JpaRepository<PersonalDiary, Long> {
    boolean existsPersonalDiaryByUserAndDate(User user, LocalDate date);

    Optional<PersonalDiary> findByIdAndUser(Long personalDiaryId, User user);

}
