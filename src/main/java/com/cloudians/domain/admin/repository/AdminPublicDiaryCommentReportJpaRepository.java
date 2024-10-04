package com.cloudians.domain.admin.repository;

import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminPublicDiaryCommentReportJpaRepository extends JpaRepository<PublicDiaryCommentReport, Long> {
    List<PublicDiaryCommentReport> findReportsByReporter(User reporter);

    List<PublicDiaryCommentReport> findReportsByReporterAndStatus(User reporter, ReportStatus status);
}