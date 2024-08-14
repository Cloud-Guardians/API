package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDiaryAnalysisRepository extends JpaRepository<PersonalDiaryAnalysis, Long> {

}
