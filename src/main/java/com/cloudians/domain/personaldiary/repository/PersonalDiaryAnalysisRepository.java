package com.cloudians.domain.personaldiary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;

@Repository
public interface PersonalDiaryAnalysisRepository extends JpaRepository<PersonalDiaryAnalysis, Long> {

    Optional<PersonalDiaryAnalysis> findByPersonalDiaryId(Long personalDiaryId);
}
