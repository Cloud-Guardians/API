package com.cloudians.domain.publicdiary.repository.comment;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicDiaryCommentJpaRepository extends JpaRepository<PublicDiaryComment, Long> {

    List<PublicDiaryComment> findByPublicDiary(PublicDiary publicDiary);

    List<PublicDiaryComment> deletePublicDiaryCommentByParentCommentId(Long parentCommentId);
}