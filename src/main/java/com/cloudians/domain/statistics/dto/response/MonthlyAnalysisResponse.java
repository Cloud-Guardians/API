package com.cloudians.domain.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyAnalysisResponse {
    private Long monthlyId;
    private String userEmail;
    private String monthlyDate;
    private int totalDiary;
    private int totalAnswer;
    private int monthlyHappy;
    private int monthlySad;
    private int monthlyAngry;
    private int monthlyUneasy;
    private int monthlyBoring;
    private String monthlyElement;
    private String mostElementTop3;
}
