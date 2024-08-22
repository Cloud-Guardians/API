package com.cloudians.domain.publicdiary.entity.like;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class PublicDiaryCommentLikeLink extends LikeLink {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_comment_id")
    private PublicDiaryComment publicDiaryComment;

    public static PublicDiaryCommentLikeLink toEntity(PublicDiaryComment publicDiaryComment, User user) {
        return PublicDiaryCommentLikeLink.builder()
                .publicDiaryComment(publicDiaryComment)
                .user(user)
                .build();
    }
}
