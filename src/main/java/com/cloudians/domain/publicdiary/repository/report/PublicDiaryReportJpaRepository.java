package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicDiaryReportJpaRepository extends JpaRepository<PublicDiaryReport, Long> {
    boolean existsByReporterAndReportedDiary(User reporter, PublicDiary reportedDiary);
}