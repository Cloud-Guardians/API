package com.cloudians.domain.publicdiary.repository.like;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryCommentLikeLink;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicDiaryCommentLikeLinkJpaRepository extends JpaRepository<PublicDiaryCommentLikeLink, Long> {
    Optional<PublicDiaryCommentLikeLink> findByPublicDiaryCommentAndUser(PublicDiaryComment publicDiaryComment, User user);
}
