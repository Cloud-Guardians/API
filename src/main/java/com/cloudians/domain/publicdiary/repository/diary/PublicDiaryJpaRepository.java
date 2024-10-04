package com.cloudians.domain.publicdiary.repository.diary;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicDiaryJpaRepository extends JpaRepository<PublicDiary, Long> {
    boolean existsByPersonalDiaryId(Long personalDiaryId);

    Optional<PublicDiary> findByPersonalDiaryId(Long personalDiaryId);

}