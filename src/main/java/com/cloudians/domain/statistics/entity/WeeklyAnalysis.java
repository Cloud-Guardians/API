package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.dto.response.WeeklyAnalysisResponse;
import com.cloudians.domain.user.entity.User;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
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

    @Builder.Default
    @Column(name="total_diary")
    private int totalDiary =0;

    @Builder.Default
    @Column(name="total_answer")
    private int totalAnswer=0;

    @Builder.Default
    @Column(name="weekly_joy")
    private int weeklyJoy=0;

    @Builder.Default
    @Column(name="weekly_sadness")
    private int weeklySadness=0;

    @Builder.Default
    @Column(name="weekly_anger")
    private int weeklyAnger=0;

    @Builder.Default
    @Column(name="weekly_anxiety")
    private int weeklyAnxiety=0;

    @Builder.Default
    @Column(name="weekly_boredom")
    private int weeklyBoredom=0;


    public void addAnalysisEmotion(PersonalDiaryEmotion emotion) {
        weeklyJoy += emotion.getJoy();
        weeklySadness += emotion.getSadness();
        weeklyAnxiety += emotion.getAnxiety();
        weeklyAnger += emotion.getAnger();
        weeklyBoredom += emotion.getBoredom();
    }

    public void subtractAnalysisEmotion(PersonalDiaryEmotion emotion) {
        weeklyJoy -= emotion.getJoy();
        weeklySadness -= emotion.getSadness();
        weeklyAnxiety -= emotion.getAnxiety();
        weeklyAnger -= emotion.getAnger();
        weeklyBoredom -= emotion.getBoredom();
    }

    public void updateWhisperCount(int count) {
        totalAnswer = count;
    }

    public void updateDiaryCount(int count) {
        totalDiary += count;
    }

}
