package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublicDiaryCommentReportJpaRepository extends JpaRepository<PublicDiaryCommentReport, Long> {
    boolean existsByReporterAndReportedComment(User reporter, PublicDiaryComment reportedComment);

    List<PublicDiaryCommentReport> findByStatus(ReportStatus status);

    List<PublicDiaryCommentReport> findByReportedComment(PublicDiaryComment reportedComment);

    Optional<PublicDiaryCommentReport> findById(String id);
}