package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.checker.units.qual.N;

@Getter
@SuperBuilder
public class MonthlyAnalysisResponse extends AnalysisResponse{
    private String monthlyElement;
    private String mostElementTop3;


    public static MonthlyAnalysisResponse of(MonthlyAnalysis analysis, FiveElement element) {
        EmotionAnalysisResponse emotionAnalysisResponse = new EmotionAnalysisResponse(analysis);
        TotalCountResponse totalCountResponse = new TotalCountResponse(analysis);
        return MonthlyAnalysisResponse.builder()
                .date(analysis.getMonthlyDate())
                .totalCount(totalCountResponse)
                .emotion(emotionAnalysisResponse)
                .monthlyElement(element.getName())
                .mostElementTop3(analysis.getMostElementTop3())
                .build();

    }
}
