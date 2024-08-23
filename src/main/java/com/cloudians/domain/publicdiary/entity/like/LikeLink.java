package com.cloudians.domain.publicdiary.entity.like;

import com.cloudians.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class LikeLink {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_email")
    private User user;
}