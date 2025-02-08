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

    public List<MonthlyAnalysis> findMonthlyStatsBatch(User user, LocalDate startDate, LocalDate endDate) {
        return queryFactory
                .select(Projections.constructor(MonthlyAnalysis.class,
                        monthlyAnalysis.monthlyId,
                        monthlyAnalysis.user,
                        monthlyAnalysis.monthlyDate,
                        monthlyAnalysis.totalDiary.sum(),
                        monthlyAnalysis.totalAnswer.sum(),
                        monthlyAnalysis.monthlyJoy.sum(),
                        monthlyAnalysis.monthlySadness.sum(),
                        monthlyAnalysis.monthlyAnger.sum(),
                        monthlyAnalysis.monthlyAnxiety.sum(),
                        monthlyAnalysis.monthlyBoredom.sum(),
                        monthlyAnalysis.monthlyElement,
                        monthlyAnalysis.mostElementTop3))
                .from(monthlyAnalysis)
                .where(monthlyAnalysis.user.eq(user)
                        .and(monthlyAnalysis.monthlyDate.between(
                                startDate.toString().substring(0, 7).replace("-", ""),
                                endDate.toString().substring(0, 7).replace("-", ""))))
                .groupBy(monthlyAnalysis.monthlyDate)
                .fetch();
    }

    public void updateEmotionStats(User user, String yearMonth) {
        queryFactory
                .update(monthlyAnalysis)
                .set(monthlyAnalysis.monthlyJoy,
                        queryFactory.select(personalDiaryEmotion.joy.sum())
                                .from(personalDiaryEmotion)
                                .where(personalDiaryEmotion.user.eq(user)
                                        .and(Expressions.dateTemplate(String.class, "{0}-%1$tm", personalDiaryEmotion.date.year(), personalDiaryEmotion.date.month()).eq(yearMonth)))
                )
                .set(monthlyAnalysis.monthlySadness,
                        queryFactory.select(personalDiaryEmotion.sadness.sum())
                                .from(personalDiaryEmotion)
                                .where(personalDiaryEmotion.user.eq(user)
                                        .and(Expressions.dateTemplate(String.class, "{0}-%1$tm", personalDiaryEmotion.date.year(), personalDiaryEmotion.date.month()).eq(yearMonth)))
                )
                .set(monthlyAnalysis.monthlyAnger,
                        queryFactory.select(personalDiaryEmotion.anger.sum())
                                .from(personalDiaryEmotion)
                                .where(personalDiaryEmotion.user.eq(user)
                                        .and(Expressions.dateTemplate(String.class, "{0}-%1$tm", personalDiaryEmotion.date.year(), personalDiaryEmotion.date.month()).eq(yearMonth)))
                )
                .set(monthlyAnalysis.monthlyAnxiety,
                        queryFactory.select(personalDiaryEmotion.anxiety.sum())
                                .from(personalDiaryEmotion)
                                .where(personalDiaryEmotion.user.eq(user)
                                        .and(Expressions.dateTemplate(String.class, "{0}-%1$tm", personalDiaryEmotion.date.year(), personalDiaryEmotion.date.month()).eq(yearMonth)))
                )
                .set(monthlyAnalysis.monthlyBoredom,
                        queryFactory.select(personalDiaryEmotion.boredom.sum())
                                .from(personalDiaryEmotion)
                                .where(personalDiaryEmotion.user.eq(user)
                                        .and(Expressions.dateTemplate(String.class, "{0}-%1$tm", personalDiaryEmotion.date.year(), personalDiaryEmotion.date.month()).eq(yearMonth)))
                )
                .where(monthlyAnalysis.user.eq(user)
                        .and(monthlyAnalysis.monthlyDate.eq(yearMonth))) // where 조건은 모든 set() 이후에 적용
                .execute(); // executeUpdate 호출
    }


    
    
    

}
