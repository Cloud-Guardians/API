package com.cloudians.domain.publicdiary.dto.response;

import com.cloudians.domain.publicdiary.entity.PublicDiary;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PublicDiaryCreateResponse {
    private Long publicDiaryId;

    private String userEmail;

    private String title;

    private String content;

    private String photoUrl;

    private LocalDate date;

    private LocalDateTime createdAt;

    @Builder
    private PublicDiaryCreateResponse(Long publicDiaryId, String userEmail, String title, String content, String photoUrl, LocalDateTime createdAt, LocalDate date) {
        this.publicDiaryId = publicDiaryId;
        this.userEmail = userEmail;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.date = date;
    }

    public static PublicDiaryCreateResponse of(PublicDiary publicDiary, User user) {
        return PublicDiaryCreateResponse.builder()
                .publicDiaryId(publicDiary.getId())
                .userEmail(user.getUserEmail())
                .title(publicDiary.getPersonalDiary().getTitle())
                .content(publicDiary.getPersonalDiary().getContent())
                .photoUrl(publicDiary.getPersonalDiary().getPhotoUrl())
                .date(publicDiary.getPersonalDiary().getDate())
                .createdAt(publicDiary.getCreatedAt())
                .build();
    }
}