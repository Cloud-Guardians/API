package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicDiaryCommentReportJpaRepository extends JpaRepository<PublicDiaryCommentReport, Long> {
    boolean existsByReporterAndReportedComment(User reporter, PublicDiaryComment reportedComment);
}
