package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.user.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name="monthly_analysis",
indexes = {
        @Index(name ="idx_user_date", columnList="user_email,monthly_date"),
        @Index(name="idx_monthly_date", columnList = "monthly_date")
})
@Entity
public class MonthlyAnalysis {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="monthly_id")
    private Long monthlyId;
    
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
    

    
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
		.userEmail(user.getUserEmail())
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

    public void addAnalysisEmotion(PersonalDiaryEmotion emotion) {
        monthlyJoy += emotion.getJoy();
        monthlySadness += emotion.getSadness();
        monthlyAnxiety += emotion.getAnxiety();
        monthlyAnger += emotion.getAnger();
        monthlyBoredom += emotion.getBoredom();
    }

    public void subtractAnalysisEmotion(PersonalDiaryEmotion emotion) {
        monthlyJoy -= emotion.getJoy();
        monthlySadness -= emotion.getSadness();
        monthlyAnxiety -= emotion.getAnxiety();
        monthlyAnger -= emotion.getAnger();
        monthlyBoredom -= emotion.getBoredom();
    }
    

}
