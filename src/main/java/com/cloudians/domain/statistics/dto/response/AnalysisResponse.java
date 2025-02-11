package com.cloudians.domain.statistics.dto.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
public class AnalysisResponse {
    private String date;
    private TotalCountResponse totalCount;
    private EmotionAnalysisResponse emotion;
}
