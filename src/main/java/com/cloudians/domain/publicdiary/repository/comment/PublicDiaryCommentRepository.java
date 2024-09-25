package com.cloudians.domain.publicdiary.repository.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicDiaryCommentRepository {

    void save(PublicDiaryComment publicDiaryComment);

    void delete(PublicDiaryComment publicDiaryComment);

    Optional<PublicDiaryComment> findById(Long publicDiaryCommentId);

    void deleteAll(List<PublicDiaryComment> commentsInPublicDiary);

    void deleteChildComments(Long parentCommentId);

    List<PublicDiaryComment> findByPublicDiary(PublicDiary publicDiary);

    List<PublicDiaryComment> findCommentsOrderByCreatedAtAsc(Long publicDiaryId, Long cursor, Long count);

    List<PublicDiaryComment> findChildCommentsOrderByAsc(Long cursor, Long count, Long parentCommentId);

    Long getPublicDiaryCommentsCount(PublicDiary publicDiary);
}
