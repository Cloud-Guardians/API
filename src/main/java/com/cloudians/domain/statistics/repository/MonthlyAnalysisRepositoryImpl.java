package com.cloudians.domain.statistics.repository;

import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.cloudians.domain.personaldiary.entity.QPersonalDiaryEmotion.personalDiaryEmotion;
import static com.cloudians.domain.statistics.entity.QMonthlyAnalysis.monthlyAnalysis;

@Repository
@RequiredArgsConstructor
public class MonthlyAnalysisRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    @Cacheable(value = "monthlyAnalysis", key = "#user.userEmail + #yearMonth")
    public Optional<MonthlyAnalysis> findByUserAndMonthlyDate(User user, String yearMonth) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(monthlyAnalysis)
                        .where(monthlyAnalysis.user.eq(user)
                                .and(monthlyAnalysis.monthlyDate.eq(yearMonth)))
                        .fetchOne()
        );
    }
}
