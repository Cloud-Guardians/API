package com.cloudians.domain.publicdiary.repository;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.cloudians.domain.publicdiary.entity.comment.QPublicDiaryComment.publicDiaryComment;

@Repository
@RequiredArgsConstructor
public class PublicDiaryCommentRepositoryImpl {
    private final PublicDiaryCommentJpaRepository publicDiaryCommentJpaRepository;
    private final JPAQueryFactory q;

    public void save(PublicDiaryComment publicDiaryComment) {
        publicDiaryCommentJpaRepository.save(publicDiaryComment);
    }

    public void delete(PublicDiaryComment publicDiaryComment) {
        publicDiaryCommentJpaRepository.delete(publicDiaryComment);
    }

    public Optional<PublicDiaryComment> findById(Long publicDiaryCommentId) {
        return publicDiaryCommentJpaRepository.findById(publicDiaryCommentId);
    }

    public Optional<PublicDiaryComment> findByIdAndUser(Long publicDiaryCommentId, User user) {
        return publicDiaryCommentJpaRepository.findByIdAndAuthor(publicDiaryCommentId, user);
    }

    private BooleanExpression getGt(Long cursor) {
        return cursor == null ? null : publicDiaryComment.id.gt(cursor);
    }

    public List<PublicDiaryComment> findCommentsOrderByCreatedAtAsc(Long publicDiaryId, Long cursor, Long count) {
        return q.selectFrom(publicDiaryComment)
                .where(publicDiaryComment.publicDiary.id.eq(publicDiaryId)
                        .and(getGt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryComment.id.asc())
                .fetch();
    }

    public List<PublicDiaryComment> findByPublicDiary(PublicDiary publicDiary) {
        return publicDiaryCommentJpaRepository.findByPublicDiary(publicDiary);
    }

    public void deleteAll(List<PublicDiaryComment> commentsInPublicDiary) {
        publicDiaryCommentJpaRepository.deleteAll(commentsInPublicDiary);
    }

    public void deleteChildComments(Long parentCommentId) {
        publicDiaryCommentJpaRepository.deletePublicDiaryCommentByParentCommentId(parentCommentId);
    }

    public List<PublicDiaryComment> findChildCommentsOrderByAsc(Long cursor, Long count, Long parentCommentId) {
        return q.selectFrom(publicDiaryComment)
                .where(publicDiaryComment.parentCommentId.eq(parentCommentId)
                        .and(getGt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryComment.id.asc())
                .fetch();
    }
}
