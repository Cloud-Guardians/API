package com.cloudians.domain.statistics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.user.entity.User;

@Repository
public interface MonthlyAnalysisJPARepository extends JpaRepository<MonthlyAnalysis, Long> {
    
    Optional<MonthlyAnalysis> findByUser(User user);
    Optional<MonthlyAnalysis> findByUserAndMonthlyDate(User user, String date);

}
