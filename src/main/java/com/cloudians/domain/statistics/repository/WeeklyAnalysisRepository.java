package com.cloudians.domain.statistics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.statistics.entity.WeeklyAnalysis;

@Repository
public interface WeeklyAnalysisRepository extends JpaRepository<WeeklyAnalysis, Long> {

    Optional<WeeklyAnalysis> findByUserEmailAndWeeklyDate(String userEmail, String date);

}
