package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.dto.response.WeeklyAnalysisResponse;
import com.cloudians.domain.user.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class WeeklyAnalysis {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="weekly_id")
    private Long weeklyId;
    
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    
    @Column(name="weekly_date")
    private String weeklyDate;
    
    @Column(name="total_diary")
    private int totalDiary;
    
    @Column(name="total_answer")
    private int totalAnswer;
    
    @Column(name="weekly_joy")
    private int weeklyJoy;
    
    @Column(name="weekly_sadness")
    private int weeklySadness;
    
    @Column(name="weekly_anger")
    private int weeklyAnger;
    
    @Column(name="weekly_anxiety")
    private int weeklyAnxiety;
    
    @Column(name="weekly_boredom")
    private int weeklyBoredom;
    
    
    public WeeklyAnalysisResponse toDto() {
  	return WeeklyAnalysisResponse.builder()
  		.userEmail(user.getUserEmail())
  		.weeklyDate(weeklyDate)
  		.totalDiary(totalDiary)
  		.totalAnswer(totalAnswer)
  		.weeklyJoy(weeklyJoy)
  		.weeklySadness(weeklySadness)
  		.weeklyAnger(weeklyAnger)
  		.weeklyAnxiety(weeklyAnxiety)
  		.weeklyBoredom(weeklyBoredom)
  		.build();
      }
}
