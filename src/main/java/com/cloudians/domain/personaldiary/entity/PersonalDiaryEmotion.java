package com.cloudians.domain.personaldiary.entity;

import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PersonalDiaryEmotion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "emotion_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Column(nullable = false)
    private int joy;

    @Column(nullable = false)
    private int sadness;

    @Column(nullable = false)
    private int anger;

    @Column(nullable = false)
    private int anxiety;

    @Column(nullable = false)
    private int boredom;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    private PersonalDiaryEmotion(User user, int joy, int sadness, int anger, int anxiety, int boredom, LocalDate date) {
        this.user = user;
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
        this.date = date;
    }
}
