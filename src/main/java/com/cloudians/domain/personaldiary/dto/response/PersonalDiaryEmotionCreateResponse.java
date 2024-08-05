package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PersonalDiaryEmotionCreateResponse {
    private Long emotionId;

    private int joy;

    private int sadness;

    private int anger;

    private int anxiety;

    private int boredom;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private PersonalDiaryEmotionCreateResponse(Long emotionId, int joy, int sadness, int anger, int anxiety, int boredom, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.emotionId = emotionId;
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PersonalDiaryEmotionCreateResponse of(PersonalDiaryEmotion personalDiaryEmotion) {
        return PersonalDiaryEmotionCreateResponse.builder()
                .emotionId(personalDiaryEmotion.getId())
                .joy(personalDiaryEmotion.getJoy())
                .sadness(personalDiaryEmotion.getSadness())
                .anger(personalDiaryEmotion.getAnger())
                .anxiety(personalDiaryEmotion.getAnxiety())
                .boredom(personalDiaryEmotion.getBoredom())
                .createdAt(personalDiaryEmotion.getCreatedAt())
                .updatedAt(personalDiaryEmotion.getUpdatedAt())
                .build();
    }
}
