package com.cloudians.domain.statistics.dto.response;

import com.cloudians.domain.user.dto.response.UserLockResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyAnalysisResponse {

    private Long weeklyId;
    private String userEmail;
    private String weeklyDate;
    private int totalDiary;
    private int totalAnswer;
    private int weeklyJoy;
    private int weeklySadness;
    private int weeklyAnger;
    private int weeklyAnxiety;
    private int weeklyBoredom;
    
  
    
}
