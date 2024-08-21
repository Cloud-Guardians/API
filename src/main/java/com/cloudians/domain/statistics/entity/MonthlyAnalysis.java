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
    
    @Column(name="monthly_happy")
    private int monthlyHappy;
    
    @Column(name="monthly_sad")
    private int monthlySad;
    
    @Column(name="monthly_angry")
    private int monthlyAngry;
    
    @Column(name="monthly_uneasy")
    private int monthlyUneasy;
    
    @Column(name="monthly_boring")
    private int monthlyBoring;
    
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
		.monthlyHappy(monthlyHappy)
		.monthlySad(monthlySad)
		.monthlyAngry(monthlyAngry)
		.monthlyUneasy(monthlyUneasy)
		.monthlyBoring(monthlyBoring)
		.monthlyElement(monthlyElement)
		.mostElementTop3(mostElementTop3)
		.build();
    }
    

}
