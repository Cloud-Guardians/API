package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDiaryRepository extends JpaRepository<PersonalDiary, Long> {
}
