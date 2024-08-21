package com.cloudians.domain.personaldiary.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PersonalDiary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "personal_diary_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "personal_diary_analysis_id")
    private PersonalDiaryAnalysis personalDiaryAnalysis;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "emotion_id")
    private PersonalDiaryEmotion emotion;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String photoUrl;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    private PersonalDiary(User user, PersonalDiaryEmotion emotion, String title, String content, String photoUrl, LocalDate date) {
        this.user = user;
        this.emotion = emotion;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.date = date;
    }

    public PersonalDiary edit(PersonalDiaryUpdateRequest request, String photoUrl) {
        if (request.getTitle() != null) {
            this.title = request.getTitle().trim();
        }
        if (request.getContent() != null) {
            this.content = request.getContent().trim();
        }
        if (photoUrl != null) {
            this.photoUrl = photoUrl;
        }

        return this;
    }

    public void deletePhotoUrl() {
        this.photoUrl = null;
    }

    public void linkPersonalDiaryAnalysis(PersonalDiaryAnalysis personalDiaryAnalysis) {
        this.personalDiaryAnalysis = personalDiaryAnalysis;
    }
}