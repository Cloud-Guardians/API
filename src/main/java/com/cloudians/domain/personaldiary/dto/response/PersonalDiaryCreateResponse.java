package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PersonalDiaryCreateResponse {
    private Long personalDiaryId;

    private String userEmail;

    private Long emotionId;

    private String title;

    private String content;

    private String photoUrl;

    private LocalDate date;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private PersonalDiaryCreateResponse(Long personalDiaryId, String userEmail, Long emotionId, String title, String content, String photoUrl, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate date) {
        this.personalDiaryId = personalDiaryId;
        this.userEmail = userEmail;
        this.emotionId = emotionId;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.date = date;
    }

    public static PersonalDiaryCreateResponse of(PersonalDiary savedPersonalDiary, User user, PersonalDiaryEmotion emotions) {
        return PersonalDiaryCreateResponse.builder()
                .personalDiaryId(savedPersonalDiary.getId())
                .userEmail(user.getUserEmail())
                .emotionId(emotions.getId())
                .title(savedPersonalDiary.getTitle())
                .content(savedPersonalDiary.getContent())
                .photoUrl(savedPersonalDiary.getPhotoUrl())
                .date(savedPersonalDiary.getDate())
                .createdAt(savedPersonalDiary.getCreatedAt())
                .updatedAt(savedPersonalDiary.getUpdatedAt())
                .build();
    }
}
