package com.cloudians.domain.personaldiary.entity;

import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PersonalDiary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "emotion_id")
    private PersonalDiaryEmotion emotion;

    private String title;

    private String content;

    private String photoUrl;

    @Builder
    private PersonalDiary(User user, PersonalDiaryEmotion emotion, String title, String content, String photoUrl) {
        this.user = user;
        this.emotion = emotion;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
    }
}
