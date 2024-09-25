package com.cloudians.domain.admin.repository;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminPublicDiaryReportJpaRepository extends JpaRepository<PublicDiaryReport, Long> {
    List<PublicDiaryReport> findByStatus(ReportStatus status);

    Optional<PublicDiaryReport> findById(Long id);

    List<PublicDiaryReport> findDuplicationById(Long id);

    List<PublicDiaryReport> findByReportedDiary(PublicDiary reportedDiary);
}