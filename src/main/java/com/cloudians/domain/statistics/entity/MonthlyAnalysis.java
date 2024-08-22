package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class MonthlyAnalysis {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="monthly_id")
    private Long monthlyId;
    
//    @ManyToOne
//    @JoinColumn(name = "user_email")
//    private User user;
    
    @Column(name="user_email")
    private String userEmail;

    
    @Column(name="monthly_date")
    private String monthlyDate;
    
    @Column(name="total_diary")
    private int totalDiary;
    
    @Column(name="total_answer")
    private int totalAnswer;
    
    @Column(name="monthly_joy")
    private int monthlyJoy;
    
    @Column(name="monthly_sadness")
    private int monthlySadness;
    
    @Column(name="monthly_anger")
    private int monthlyAnger;
    
    @Column(name="monthly_anxiety")
    private int monthlyAnxiety;
    
    @Column(name="monthly_boredom")
    private int monthlyBoredom;
    
    @Column(name="monthly_element")
    private String monthlyElement;
    
    @Column(name="most_element_top3")
    private String mostElementTop3;
    
    public MonthlyAnalysisResponse toDto() {
	return MonthlyAnalysisResponse.builder()
		.userEmail(userEmail)
		.monthlyDate(monthlyDate)
		.totalDiary(totalDiary)
		.totalAnswer(totalAnswer)
		.monthlyJoy(monthlyJoy)
		.monthlySadness(monthlySadness)
		.monthlyAnger(monthlyAnger)
		.monthlyAnxiety(monthlyAnxiety)
		.monthlyBoredom(monthlyBoredom)
		.monthlyElement(monthlyElement)
		.mostElementTop3(mostElementTop3)
		.build();
    }
    

}
