package com.cloudians.domain.publicdiary.repository;

import com.cloudians.domain.publicdiary.entity.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicDiaryRepository extends JpaRepository<PublicDiary, Long> {
    boolean existsByPersonalDiaryId(Long personalDiaryId);
}
