package com.cloudians.domain.personaldiary.entity.analysis;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Map;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor
public class PersonalDiaryAnalysis {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "diary_analysis_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "personal_diary_id")
    private PersonalDiary personalDiary;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "element_id")
    private FiveElement fiveElement;

    @Column(columnDefinition = "json")
    private String elementCharacters;

    @Column(columnDefinition = "json")
    private String harmonyTips;

    private String fortuneDetail;

    private String advice;

    @Builder
    private PersonalDiaryAnalysis(User user, PersonalDiary personalDiary, FiveElement fiveElement, String elementCharacters, String harmonyTips, String fortuneDetail, String advice) {
        this.user = user;
        this.personalDiary = personalDiary;
        this.fiveElement = fiveElement;
        this.elementCharacters = elementCharacters;
        this.harmonyTips = harmonyTips;
        this.fortuneDetail = fortuneDetail;
        this.advice = advice;
    }
}
