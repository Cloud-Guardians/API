package com.cloudians.domain.publicdiary.dto.request.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class WriteChildCommentRequest {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    private WriteChildCommentRequest(String content) {
        this.content = content;
    }

    public PublicDiaryComment toEntity(PublicDiary publicDiary, User author, Long parnetCommentId) {
        return PublicDiaryComment.builder()
                .publicDiary(publicDiary)
                .author(author)
                .content(content)
                .parentCommentId(parnetCommentId)
                .build();
    }
}
