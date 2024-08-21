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
    
//    @ManyToOne
//    @JoinColumn(name = "user_email")
//    private User user;
    
    
    @Column(name="user_email")
    private String userEmail;
    
    @Column(name="weekly_date")
    private String weeklyDate;
    
    @Column(name="total_diary")
    private int totalDiary;
    
    @Column(name="total_answer")
    private int totalAnswer;
    
    @Column(name="weekly_happy")
    private int weeklyHappy;
    
    @Column(name="weekly_sad")
    private int weeklySad;
    
    @Column(name="weekly_angry")
    private int weeklyAngry;
    
    @Column(name="weekly_uneasy")
    private int weeklyUneasy;
    
    @Column(name="weekly_boring")
    private int weeklyBoring;
    
    
    public WeeklyAnalysisResponse toDto() {
  	return WeeklyAnalysisResponse.builder()
  		.userEmail(userEmail)
  		.weeklyDate(weeklyDate)
  		.totalDiary(totalDiary)
  		.totalAnswer(totalAnswer)
  		.weeklyHappy(weeklyHappy)
  		.weeklySad(weeklySad)
  		.weeklyAngry(weeklyAngry)
  		.weeklyUneasy(weeklyUneasy)
  		.weeklyBoring(weeklyBoring)
  		.build();
      }
}
