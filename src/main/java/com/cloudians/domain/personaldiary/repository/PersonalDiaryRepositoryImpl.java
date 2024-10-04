package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonalDiaryRepositoryImpl implements PersonalDiaryRepository {
    private final PersonalDiaryJpaRepository personalDiaryJpaRepository;

    @Override
    public boolean existsPersonalDiaryByUserAndDate(User user, LocalDate date) {
        return personalDiaryJpaRepository.existsPersonalDiaryByUserAndDate(user, date);
    }

    @Override
    public Optional<PersonalDiary> findByUserAndDate(User user, LocalDate date) {
        return personalDiaryJpaRepository.findByUserAndDate(user, date);
    }

    @Override
    public Optional<List<PersonalDiary>> findListByUser(User user) {
        return personalDiaryJpaRepository.findListByUser(user);
    }

    @Override
    public Optional<List<PersonalDiary>> findPersonalDiaryByUserAndDateBetweenOrderByDate(User user, LocalDate startOfMonth, LocalDate endOfMonth) {
        return personalDiaryJpaRepository.findPersonalDiaryByUserAndDateBetweenOrderByDate(user, startOfMonth, endOfMonth);
    }

    @Override
    public PersonalDiary save(PersonalDiary personalDiary) {
        return personalDiaryJpaRepository.save(personalDiary);
    }

    @Override
    public void delete(PersonalDiary personalDiary) {
        personalDiaryJpaRepository.delete(personalDiary);
    }

    @Override
    public Optional<PersonalDiary> findById(Long id) {
        return personalDiaryJpaRepository.findById(id);
    }
}
