package com.cloudians.domain.publicdiary.repository.like;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryLikeLink;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.cloudians.domain.publicdiary.entity.like.QPublicDiaryLikeLink.publicDiaryLikeLink;

@Repository
@RequiredArgsConstructor
public class PublicDiaryLikeLinkRepositoryImpl implements PublicDiaryLikeLinkRepository {
    private final PublicDiaryLikeLinkJpaRepository publicDiaryLikeLinkJpaRepository;
    private final JPAQueryFactory q;

    @Override
    public void save(PublicDiaryLikeLink publicDiaryLikeLink) {
        publicDiaryLikeLinkJpaRepository.save(publicDiaryLikeLink);
    }

    @Override
    public void delete(PublicDiaryLikeLink publicDiaryLikeLink) {
        publicDiaryLikeLinkJpaRepository.delete(publicDiaryLikeLink);
    }

    @Override
    public Optional<PublicDiaryLikeLink> findByPublicDiaryAndUser(PublicDiary publicDiary, User user) {
        return publicDiaryLikeLinkJpaRepository.findByPublicDiaryAndUser(publicDiary, user);
    }

    @Override
    public List<PublicDiaryLikeLink> findPublicDiaryLikesOrderByDesc(Long cursor, Long count, Long publicDiaryId) {
        return q.selectFrom(publicDiaryLikeLink)
                .where(publicDiaryLikeLink.publicDiary.id.eq(publicDiaryId)
                        .and(getLt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryLikeLink.id.desc())
                .fetch();
    }

    private BooleanExpression getLt(Long cursor) {
        return cursor == null ? null : publicDiaryLikeLink.id.lt(cursor);
    }
}