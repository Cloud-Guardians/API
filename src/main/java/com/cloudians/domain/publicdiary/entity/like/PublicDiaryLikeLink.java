package com.cloudians.domain.publicdiary.entity.like;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
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
public class PublicDiaryLikeLink extends LikeLink {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_id")
    private PublicDiary publicDiary;


    public static PublicDiaryLikeLink toEntity(PublicDiary publicDiary, User user) {
        return PublicDiaryLikeLink.builder()
                .publicDiary(publicDiary)
                .user(user)
                .build();
    }
}