package com.cloudians.domain.publicdiary.repository.diary;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicDiaryJpaRepository extends JpaRepository<PublicDiary, Long> {
    boolean existsByPersonalDiaryId(Long personalDiaryId);

}