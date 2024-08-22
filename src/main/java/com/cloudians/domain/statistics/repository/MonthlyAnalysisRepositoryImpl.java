package com.cloudians.domain.statistics.repository;

import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MonthlyAnalysisRepositoryImpl {
    
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final MonthlyAnalysisJPARepository monthlyJPARepository;
    private final PersonalDiaryRepository diaryRepository;
    private final JPAQueryFactory q;
    
    
    

}
