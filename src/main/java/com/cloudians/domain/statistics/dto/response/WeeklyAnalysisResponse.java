package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
public class WeeklyAnalysisResponse extends AnalysisResponse {

    // 다음 주 예측 및 조언 필드

    public static WeeklyAnalysisResponse of(WeeklyAnalysis analysis) {
        EmotionAnalysisResponse emotionResponse = new EmotionAnalysisResponse(analysis);
        TotalCountResponse totalCountResponse = new TotalCountResponse(analysis);
        return WeeklyAnalysisResponse.builder()
                .date(analysis.getWeeklyDate())
                .totalCount(totalCountResponse)
                .emotion(emotionResponse)
                .build();
    }
    
}
