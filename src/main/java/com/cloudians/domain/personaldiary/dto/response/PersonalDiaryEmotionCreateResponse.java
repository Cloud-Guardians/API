package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonalDiaryEmotionCreateResponse {
    private Long id;

    private int joy;

    private int sadness;

    private int anger;

    private int anxiety;

    private int boredom;

    @Builder
    private PersonalDiaryEmotionCreateResponse(Long id, int joy, int sadness, int anger, int anxiety, int boredom) {
        this.id = id;
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
    }

    public static PersonalDiaryEmotionCreateResponse of(PersonalDiaryEmotion personalDiaryEmotion) {
        return PersonalDiaryEmotionCreateResponse.builder()
                .id(personalDiaryEmotion.getId())
                .joy(personalDiaryEmotion.getJoy())
                .sadness(personalDiaryEmotion.getSadness())
                .anger(personalDiaryEmotion.getAnger())
                .anxiety(personalDiaryEmotion.getAnxiety())
                .boredom(personalDiaryEmotion.getBoredom())
                .build();
    }
}
