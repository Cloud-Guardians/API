package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TotalCountResponse {
    private int totalDiary;
    private int totalWhisper;

    public TotalCountResponse(MonthlyAnalysis analysis){
        this.totalDiary = analysis.getTotalDiary();
        this.totalWhisper = analysis.getTotalAnswer();
    }

    public TotalCountResponse(WeeklyAnalysis analysis){
        this.totalDiary = analysis.getTotalDiary();
        this.totalWhisper = analysis.getTotalAnswer();
    }
}
