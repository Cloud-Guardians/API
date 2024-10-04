package com.cloudians.domain.publicdiary.entity.comment;

import com.cloudians.domain.publicdiary.dto.request.comment.EditPublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class PublicDiaryComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "public_diary_comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_id")
    private PublicDiary publicDiary;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_email")
    private User author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long likes;

    private Long parentCommentId;

    @Builder
    private PublicDiaryComment(PublicDiary publicDiary, User author, String content, Long parentCommentId) {
        this.publicDiary = publicDiary;
        this.author = author;
        this.content = content;
        this.likes = 0L;
        this.parentCommentId = parentCommentId;
    }

    public PublicDiaryComment edit(EditPublicDiaryCommentRequest request) {
        if (request.getContent() != null) {
            this.content = request.getContent().trim();
        }
        return this;
    }

    public void decreaseLikeCount() {
        likes--;
    }

    public void increaseLikeCount() {
        likes++;
    }
}
