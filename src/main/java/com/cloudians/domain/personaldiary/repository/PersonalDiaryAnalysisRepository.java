package com.cloudians.domain.personaldiary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;

@Repository
public interface PersonalDiaryAnalysisRepository extends JpaRepository<PersonalDiaryAnalysis, Long> {

    Optional<PersonalDiaryAnalysis> findByPersonalDiaryId(Long personalDiaryId);
//    @Query("SELECT pda FROM PersonalDiaryAnalysis pda WHERE pda.personalDiary IN :diaryList")
//    List<PersonalDiaryAnalysis> findByPersonalDiaryIdIn(@Param("diaryList") List<PersonalDiary> diaryList);
}
