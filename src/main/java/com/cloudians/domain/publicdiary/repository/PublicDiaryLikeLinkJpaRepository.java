package com.cloudians.domain.publicdiary.repository;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryLikeLink;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicDiaryLikeLinkJpaRepository extends JpaRepository<PublicDiaryLikeLink, Long> {
    Optional<PublicDiaryLikeLink> findByPublicDiaryAndUser(PublicDiary publicDiary, User user);
}
