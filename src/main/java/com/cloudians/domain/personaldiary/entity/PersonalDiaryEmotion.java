package com.cloudians.domain.personaldiary.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionUpdateRequest;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    public PersonalDiaryEmotion edit(PersonalDiaryEmotionUpdateRequest request) {
        if (request.getJoy() != null) {
            validateEmotionValue(request.getJoy());
            this.joy = request.getJoy();
        }
        if (request.getSadness() != null) {
            validateEmotionValue(request.getSadness());
            this.sadness = request.getSadness();
        }
        if (request.getAnger() != null) {
            validateEmotionValue(request.getAnger());
            this.anger = request.getAnger();
        }
        if (request.getAnxiety() != null) {
            validateEmotionValue(request.getAnxiety());
            this.anxiety = request.getAnxiety();
        }
        if (request.getBoredom() != null) {
            validateEmotionValue(request.getBoredom());
            this.boredom = request.getBoredom();
        }

        return this;
    }

    private void validateEmotionValue(int value) {
        if (value < 0 || value > 100) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.EMOTION_VALUE_OUT_OF_RANGE);
        }
        if (value % 10 != 0) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.EMOTION_VALUE_WRONG_INPUT);
        }
    }
}