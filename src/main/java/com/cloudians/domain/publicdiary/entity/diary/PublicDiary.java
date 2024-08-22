package com.cloudians.domain.publicdiary.entity.diary;

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
    private User author;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "personal_diary_id")
    private PersonalDiary personalDiary;

    @Column(nullable = false)
    private Long views;

    @Column(nullable = false)
    private Long likes;

    @Builder
    private PublicDiary(User author, PersonalDiary personalDiary) {
        this.author = author;
        this.personalDiary = personalDiary;
        this.views = 0L;
        this.likes = 0L;
    }

    public static PublicDiary createPublicDiary(User user, PersonalDiary personalDiary) {
        return PublicDiary.builder()
                .author(user)
                .personalDiary(personalDiary)
                .build();
    }

    public void decreaseLikeCount() {
        likes--;
    }

    public void increaseLikeCount() {
        likes++;
    }

    public void updateView(Long views) {
        this.views = views + 1L;
    }
}
