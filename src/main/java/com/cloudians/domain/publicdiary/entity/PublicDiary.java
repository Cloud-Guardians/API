package com.cloudians.domain.publicdiary.entity;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
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
public class PublicDiary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "public_diary_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "personal_diary_id")
    private PersonalDiary personalDiary;

    @Builder
    private PublicDiary(User user, PersonalDiary personalDiary) {
        this.user = user;
        this.personalDiary = personalDiary;
    }

    public static PublicDiary createPublicDiary(User user, PersonalDiary personalDiary) {
        return PublicDiary.builder()
                .user(user)
                .personalDiary(personalDiary)
                .build();
    }
}
