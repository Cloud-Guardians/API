package com.cloudians.domain.statistics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;

@Repository
public interface MonthlyAnalysisJPARepository extends JpaRepository<MonthlyAnalysis, Long> {
    
    Optional<MonthlyAnalysis> findByUserEmail(String userEmail);
    Optional<MonthlyAnalysis> findByUserEmailAndMonthlyDate(String userEmail, String date);

}
