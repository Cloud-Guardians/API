package com.cloudians.domain.publicdiary.dto.response;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PublicDiaryCommentResponse {
    private Long publicDiaryCommentId;

    private String author;

    private String content;

    private Long likes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private PublicDiaryCommentResponse(Long publicDiaryCommentId, String author, String content, Long likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.publicDiaryCommentId = publicDiaryCommentId;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PublicDiaryCommentResponse of(PublicDiaryComment publicDiaryComment) {
        return PublicDiaryCommentResponse.builder()
                .publicDiaryCommentId(publicDiaryComment.getId())
                .author(publicDiaryComment.getAuthor().getNickname())
                .content(publicDiaryComment.getContent())
                .likes(publicDiaryComment.getLikes())
                .createdAt(publicDiaryComment.getCreatedAt())
                .updatedAt(publicDiaryComment.getUpdatedAt())
                .build();
    }
}
