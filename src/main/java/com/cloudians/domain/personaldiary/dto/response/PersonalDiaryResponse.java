
package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PersonalDiaryResponse {
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
    private PersonalDiaryResponse(Long personalDiaryId, String userEmail, Long emotionId, String title, String content, String photoUrl, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate date) {
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

    public static PersonalDiaryResponse of(PersonalDiary editedPersonalDiary) {
        return PersonalDiaryResponse.builder()
                .personalDiaryId(editedPersonalDiary.getId())
                .userEmail(editedPersonalDiary.getUser().getUserEmail())
                .emotionId(editedPersonalDiary.getEmotion().getId())
                .title(editedPersonalDiary.getTitle())
                .content(editedPersonalDiary.getContent())
                .photoUrl(editedPersonalDiary.getPhotoUrl())
                .date(editedPersonalDiary.getDate())
                .createdAt(editedPersonalDiary.getCreatedAt())
                .updatedAt(editedPersonalDiary.getUpdatedAt())
                .build();
    }
}
