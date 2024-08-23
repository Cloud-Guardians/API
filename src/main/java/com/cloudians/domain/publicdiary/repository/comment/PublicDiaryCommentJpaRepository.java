package com.cloudians.domain.publicdiary.repository.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublicDiaryCommentJpaRepository extends JpaRepository<PublicDiaryComment, Long> {
    Optional<PublicDiaryComment> findByIdAndAuthor(Long publicDiaryId, User author);

    List<PublicDiaryComment> findByPublicDiary(PublicDiary publicDiary);

    List<PublicDiaryComment> deletePublicDiaryCommentByParentCommentId(Long parentCommentId);
}
