package com.cloudians.domain.personaldiary.entity.analysis;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import com.cloudians.global.exception.JsonException;
import com.cloudians.global.exception.JsonExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class PersonalDiaryAnalysis extends BaseTimeEntity {

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

    public static PersonalDiaryAnalysis createPersonalDiaryAnalysis(User user, PersonalDiary personalDiary, FiveElement element, List<String> characters, String harmonyTipsJson, String[] analysisResults) {
        String elementCharactersJson = getCharactersJson(characters);

        return PersonalDiaryAnalysis.builder()
                .user(user)
                .personalDiary(personalDiary)
                .fiveElement(element)
                .elementCharacters(elementCharactersJson)
                .harmonyTips(harmonyTipsJson)
                .fortuneDetail(analysisResults[1])
                .advice(analysisResults[2])
                .build();
    }

    private static String getCharactersJson(List<String> characters) {
        ObjectMapper objectMapper = new ObjectMapper();
        String elementCharactersJson;
        try {
            elementCharactersJson = objectMapper.writeValueAsString(characters);
        } catch (JsonProcessingException e) {
            throw new JsonException(JsonExceptionType.INVALID_JSON_FORMAT);
        }
        return elementCharactersJson;
    }


    public PersonalDiaryAnalysis edit(User user, PersonalDiary personalDiary, FiveElement element, List<String> characters, String harmonyTipsJson, String[] analysisResults) {
        String elementCharactersJson = getCharactersJson(characters);

        this.user = user;
        this.personalDiary = personalDiary;
        this.fiveElement = element;
        this.elementCharacters = elementCharactersJson;
        this.harmonyTips = harmonyTipsJson;
        this.fortuneDetail = analysisResults[1];
        this.advice = analysisResults[2];
        return this;
    }
}
