package com.cloudians.domain.personaldiary.entity;

import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PersonalDiaryEmotion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    private int joy;

    private int sadness;

    private int anger;

    private int anxiety;

    private int boredom;

    @Builder
    private PersonalDiaryEmotion(int joy, int sadness, int anger, int anxiety, int boredom) {
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
    }
}
