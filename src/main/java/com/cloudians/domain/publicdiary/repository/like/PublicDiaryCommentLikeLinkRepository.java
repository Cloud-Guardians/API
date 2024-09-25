package com.cloudians.domain.publicdiary.repository.like;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryCommentLikeLink;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicDiaryCommentLikeLinkRepository {
    Optional<PublicDiaryCommentLikeLink> findByPublicDiaryCommentAndUser(PublicDiaryComment publicDiaryComment, User user);

    void save(PublicDiaryCommentLikeLink publicDiaryCommentLikeLink);

    void delete(PublicDiaryCommentLikeLink publicDiaryCommentLikeLink);

    List<PublicDiaryCommentLikeLink> findPublicDiaryCommentLikesOrderByDesc(Long cursor, Long count, Long publicDiaryCommentId);
}
