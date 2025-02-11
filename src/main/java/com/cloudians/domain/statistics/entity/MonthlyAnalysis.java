package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.user.entity.User;

import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
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

    @Builder.Default
    @Column(name="total_diary")
    private int totalDiary = 0;

    @Builder.Default
    @Column(name="total_answer")
    private int totalAnswer = 0;

    @Builder.Default
    @Column(name="monthly_joy")
    private int monthlyJoy = 0;

    @Builder.Default
    @Column(name="monthly_sadness")
    private int monthlySadness = 0;

    @Builder.Default
    @Column(name="monthly_anger")
    private int monthlyAnger = 0;

    @Builder.Default
    @Column(name="monthly_anxiety")
    private int monthlyAnxiety = 0;

    @Builder.Default
    @Column(name="monthly_boredom")
    private int monthlyBoredom = 0;


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

//    @Builder
//    public MonthlyAnalysis (User user, String monthlyDate) {
//      this.user = user;
//      this.monthlyDate = monthlyDate;
//      this.totalAnswer = 0;
//      this.totalDiary = 0;
//    }


    public void addAnalysisEmotion(PersonalDiaryEmotion emotion) {
        totalDiary ++;
        monthlyJoy += emotion.getJoy();
        monthlySadness += emotion.getSadness();
        monthlyAnxiety += emotion.getAnxiety();
        monthlyAnger += emotion.getAnger();
        monthlyBoredom += emotion.getBoredom();
    }

    public void subtractAnalysisEmotion(PersonalDiaryEmotion emotion) {
        totalDiary --;
        monthlyJoy -= emotion.getJoy();
        monthlySadness -= emotion.getSadness();
        monthlyAnxiety -= emotion.getAnxiety();
        monthlyAnger -= emotion.getAnger();
        monthlyBoredom -= emotion.getBoredom();
    }

    public void addWhisperCount() {
        totalAnswer ++;
    }

    public void subtractWhisperCount() {
        totalAnswer --;
    }
    

}
