package com.cloudians.domain.publicdiary.dto.response;

import com.cloudians.domain.publicdiary.entity.PublicDiary;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PublicDiaryResponse {
    private Long publicDiaryId;

    private String userEmail;

    private String title;

    private String content;

    private String photoUrl;

    private LocalDate date;

    private Long views;

    private Long likes;

    private LocalDateTime createdAt;

    @Builder
    private PublicDiaryResponse(Long publicDiaryId, String userEmail, String title, String content, String photoUrl, LocalDate date, Long views, Long likes, LocalDateTime createdAt) {
        this.publicDiaryId = publicDiaryId;
        this.userEmail = userEmail;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.date = date;
        this.views = views;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public static PublicDiaryResponse of(PublicDiary publicDiary, User user) {
        return PublicDiaryResponse.builder()
                .publicDiaryId(publicDiary.getId())
                .userEmail(user.getUserEmail())
                .title(publicDiary.getPersonalDiary().getTitle())
                .content(publicDiary.getPersonalDiary().getContent())
                .photoUrl(publicDiary.getPersonalDiary().getPhotoUrl())
                .date(publicDiary.getPersonalDiary().getDate())
                .views(publicDiary.getViews())
                .likes(publicDiary.getLikes())
                .createdAt(publicDiary.getCreatedAt())
                .build();
    }
}