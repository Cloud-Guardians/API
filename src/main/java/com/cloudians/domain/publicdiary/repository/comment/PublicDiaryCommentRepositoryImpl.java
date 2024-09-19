package com.cloudians.domain.publicdiary.repository.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.cloudians.domain.publicdiary.entity.comment.QPublicDiaryComment.publicDiaryComment;

@Repository
@RequiredArgsConstructor
public class PublicDiaryCommentRepositoryImpl implements PublicDiaryCommentRepository {
    private final PublicDiaryCommentJpaRepository publicDiaryCommentJpaRepository;
    private final JPAQueryFactory q;

    @Override
    public void save(PublicDiaryComment publicDiaryComment) {
        publicDiaryCommentJpaRepository.save(publicDiaryComment);
    }

    @Override
    public void delete(PublicDiaryComment publicDiaryComment) {
        publicDiaryCommentJpaRepository.delete(publicDiaryComment);
    }

    @Override
    public Optional<PublicDiaryComment> findById(Long publicDiaryCommentId) {
        return publicDiaryCommentJpaRepository.findById(publicDiaryCommentId);
    }


    @Override
    public List<PublicDiaryComment> findCommentsOrderByCreatedAtAsc(Long publicDiaryId, Long cursor, Long count) {
        return q.selectFrom(publicDiaryComment)
                .where(publicDiaryComment.publicDiary.id.eq(publicDiaryId)
                        .and(getGt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryComment.id.asc())
                .fetch();
    }

    @Override
    public List<PublicDiaryComment> findByPublicDiary(PublicDiary publicDiary) {
        return publicDiaryCommentJpaRepository.findByPublicDiary(publicDiary);
    }

    @Override
    public void deleteAll(List<PublicDiaryComment> commentsInPublicDiary) {
        publicDiaryCommentJpaRepository.deleteAll(commentsInPublicDiary);
    }

    @Override
    public void deleteChildComments(Long parentCommentId) {
        publicDiaryCommentJpaRepository.deletePublicDiaryCommentByParentCommentId(parentCommentId);
    }

    @Override
    public List<PublicDiaryComment> findChildCommentsOrderByAsc(Long cursor, Long count, Long parentCommentId) {
        return q.selectFrom(publicDiaryComment)
                .where(publicDiaryComment.parentCommentId.eq(parentCommentId)
                        .and(getGt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiaryComment.id.asc())
                .fetch();
    }

    @Override
    public Long getPublicDiaryCommentsCount(PublicDiary publicDiary) {
        return q.select(publicDiaryComment.count())
                .from(publicDiaryComment)
                .where(publicDiaryComment.publicDiary.eq(publicDiary))
                .fetchOne();
    }

    private BooleanExpression getGt(Long cursor) {
        return cursor == null ? null : publicDiaryComment.id.gt(cursor);
    }
}
