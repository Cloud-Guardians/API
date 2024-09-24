package com.cloudians.domain.publicdiary.dto.response.comment;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PublicDiaryCommentResponse {
    private Long publicDiaryCommentId;

    private Long parentCommentId;

    private UserProfileResponse author;

    private String content;

    private Long likes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private PublicDiaryCommentResponse(Long publicDiaryCommentId, Long parentCommentId, UserProfileResponse author, String content, Long likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.publicDiaryCommentId = publicDiaryCommentId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PublicDiaryCommentResponse of(PublicDiaryComment publicDiaryComment) {
        return PublicDiaryCommentResponse.builder()
                .publicDiaryCommentId(publicDiaryComment.getId())
                .parentCommentId(publicDiaryComment.getParentCommentId())
                .author(UserProfileResponse.from(publicDiaryComment.getAuthor()))
                .content(publicDiaryComment.getContent())
                .likes(publicDiaryComment.getLikes())
                .createdAt(publicDiaryComment.getCreatedAt())
                .updatedAt(publicDiaryComment.getUpdatedAt())
                .build();
    }
}
