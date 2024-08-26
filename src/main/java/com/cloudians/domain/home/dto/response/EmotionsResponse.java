package com.cloudians.domain.home.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmotionsResponse {
    private Long emotionId;

    private int joy;

    private int sadness;

    private int anger;

    private int anxiety;

    private int boredom;

    @Builder
    private EmotionsResponse(Long emotionId, int joy, int sadness, int anger, int anxiety, int boredom) {
        this.emotionId = emotionId;
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
    }

    public static EmotionsResponse of(PersonalDiaryEmotion emotion) {
        return EmotionsResponse.builder()
                .emotionId(emotion.getId())
                .joy(emotion.getJoy())
                .sadness(emotion.getSadness())
                .anger(emotion.getAnger())
                .anxiety(emotion.getAnxiety())
                .boredom(emotion.getBoredom())
                .build();
    }
}