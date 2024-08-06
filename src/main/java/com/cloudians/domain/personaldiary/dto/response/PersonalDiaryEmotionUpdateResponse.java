package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PersonalDiaryEmotionUpdateResponse {
    private Long emotionId;

    private String userEmail;

    private int joy;

    private int sadness;

    private int anger;

    private int anxiety;

    private int boredom;

    private LocalDate date;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private PersonalDiaryEmotionUpdateResponse(Long emotionId, String userEmail, int joy, int sadness, int anger, int anxiety, int boredom, LocalDate date, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.emotionId = emotionId;
        this.userEmail = userEmail;
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PersonalDiaryEmotionUpdateResponse of(PersonalDiaryEmotion editedEmotions) {
        return PersonalDiaryEmotionUpdateResponse.builder()
                .emotionId(editedEmotions.getId())
                .userEmail(editedEmotions.getUser().getUserEmail())
                .joy(editedEmotions.getJoy())
                .sadness(editedEmotions.getSadness())
                .anger(editedEmotions.getAnger())
                .anxiety(editedEmotions.getAnxiety())
                .boredom(editedEmotions.getBoredom())
                .date(editedEmotions.getDate())
                .createdAt(editedEmotions.getCreatedAt())
                .updatedAt(editedEmotions.getUpdatedAt())
                .build();
    }
}
