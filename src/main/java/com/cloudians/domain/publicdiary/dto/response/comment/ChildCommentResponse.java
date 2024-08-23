package com.cloudians.domain.publicdiary.dto.response.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChildCommentResponse {
    private Long publicDiaryCommentId;

    private Long parentCommentId;

    private String author;

    private String content;

    private Long likes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private ChildCommentResponse(Long publicDiaryCommentId, Long parentCommentId, String author, String content, Long likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.publicDiaryCommentId = publicDiaryCommentId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ChildCommentResponse of(PublicDiaryComment childComment) {
        return ChildCommentResponse.builder()
                .publicDiaryCommentId(childComment.getId())
                .parentCommentId(childComment.getParentCommentId())
                .author(childComment.getAuthor().getNickname())
                .content(childComment.getContent())
                .likes(childComment.getLikes())
                .createdAt(childComment.getCreatedAt())
                .updatedAt(childComment.getUpdatedAt())
                .build();
    }
}
