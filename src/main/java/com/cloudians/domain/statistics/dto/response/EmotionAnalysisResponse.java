package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmotionAnalysisResponse {
    private int joy;
    private int sadness;
    private int anger;
    private int anxiety;
    private int boredom;

    public EmotionAnalysisResponse(MonthlyAnalysis analysis) {
        this.joy = analysis.getMonthlyJoy();
        this.sadness = analysis.getMonthlySadness();
        this.anger = analysis.getMonthlyAnger();
        this.anxiety = analysis.getMonthlyAnxiety();
        this.boredom = analysis.getMonthlyBoredom();
    }

    public EmotionAnalysisResponse(WeeklyAnalysis analysis) {
        this.joy = analysis.getWeeklyJoy();
        this.sadness = analysis.getWeeklySadness();
        this.anger = analysis.getWeeklyAnger();
        this.anxiety = analysis.getWeeklyAnxiety();
        this.boredom = analysis.getWeeklyBoredom();
    }

}
