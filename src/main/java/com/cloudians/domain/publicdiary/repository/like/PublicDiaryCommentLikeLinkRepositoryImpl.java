package com.cloudians.domain.publicdiary.repository.like;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryCommentLikeLink;
import com.cloudians.domain.publicdiary.repository.like.PublicDiaryCommentLikeLinkJpaRepository;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.cloudians.domain.publicdiary.entity.like.QPublicDiaryCommentLikeLink.publicDiaryCommentLikeLink;


@Repository
@RequiredArgsConstructor
public class PublicDiaryCommentLikeLinkRepositoryImpl {
    private final PublicDiaryCommentLikeLinkJpaRepository publicDiaryCommentLikeLinkJpaRepository;
    private final JPAQueryFactory q;

    public void save(PublicDiaryCommentLikeLink publicDiaryCommentLikeLink) {
        publicDiaryCommentLikeLinkJpaRepository.save(publicDiaryCommentLikeLink);
    }

    public void delete(PublicDiaryCommentLikeLink publicDiaryCommentLikeLink) {
        publicDiaryCommentLikeLinkJpaRepository.delete(publicDiaryCommentLikeLink);
    }

    public Optional<PublicDiaryCommentLikeLink> findByPublicDiaryCommentAndUser(PublicDiaryComment publicDiaryComment, User user) {
        return publicDiaryCommentLikeLinkJpaRepository.findByPublicDiaryCommentAndUser(publicDiaryComment, user);
    }

    public List<PublicDiaryCommentLikeLink> findPublicDiaryCommentLikesOrderByDesc(Long cursor, Long count, Long publicDiaryCommentId) {
        return q.selectFrom(publicDiaryCommentLikeLink)
                .where(publicDiaryCommentLikeLink.publicDiaryComment.id.eq(publicDiaryCommentId)
                        .and(getLt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryCommentLikeLink.id.desc())
                .fetch();
    }

    private BooleanExpression getLt(Long cursor) {
        return cursor == null ? null : publicDiaryCommentLikeLink.id.lt(cursor);
    }

}