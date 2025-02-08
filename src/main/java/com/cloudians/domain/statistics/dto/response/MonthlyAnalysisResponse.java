package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
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
    private int monthlyJoy;
    private int monthlySadness;
    private int monthlyAnger;
    private int monthlyAnxiety;
    private int monthlyBoredom;
    private String monthlyElement;
    private String mostElementTop3;


    public static MonthlyAnalysisResponse of(MonthlyAnalysis analysis) {
        return MonthlyAnalysisResponse.builder()
                .monthlyId(analysis.getMonthlyId())
                .userEmail(analysis.getUser().getUserEmail())
                .monthlyDate(analysis.getMonthlyDate())
                .totalDiary(analysis.getTotalDiary())
                .totalAnswer(analysis.getTotalAnswer())
                .monthlyJoy(analysis.getMonthlyJoy())
                .monthlySadness(analysis.getMonthlySadness())
                .monthlyAnger(analysis.getMonthlyAnger())
                .monthlyAnxiety(analysis.getMonthlyAnxiety())
                .monthlyBoredom(analysis.getMonthlyBoredom())
                .monthlyElement(analysis.getMonthlyElement())
                .mostElementTop3(analysis.getMostElementTop3())
                .build();

    }
}
