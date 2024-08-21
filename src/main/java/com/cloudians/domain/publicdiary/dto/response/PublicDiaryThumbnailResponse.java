package com.cloudians.domain.publicdiary.dto.response;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PublicDiaryThumbnailResponse {
    private String title;

    private String content;

    private String author;

    private LocalDateTime timestamp;

    private String photoUrl;

    private Long totalLikeCount;

    private Long totalCommentsCount;

    private Long views;

    @Builder
    private PublicDiaryThumbnailResponse(String title, String content, String author, LocalDateTime timestamp, String photoUrl, Long totalLikeCount, Long totalCommentsCount, Long views) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
        this.totalLikeCount = totalLikeCount;
        this.totalCommentsCount = totalCommentsCount;
        this.views = views;
    }

    public static PublicDiaryThumbnailResponse of(PublicDiary publicDiary) {
        return PublicDiaryThumbnailResponse.builder()
                .title(publicDiary.getPersonalDiary().getTitle())
                .content(publicDiary.getPersonalDiary().getContent())
                .author(publicDiary.getAuthor().getNickname())
                .timestamp(publicDiary.getCreatedAt())
                .photoUrl(publicDiary.getPersonalDiary().getPhotoUrl())
                .views(publicDiary.getViews())
                .build();
    }
}
