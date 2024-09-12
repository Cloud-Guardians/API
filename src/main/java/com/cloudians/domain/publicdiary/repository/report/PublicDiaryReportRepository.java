package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicDiaryReportRepository {

    void save(PublicDiaryReport publicDiaryReport);

    boolean existsByReporterAndReportedDiary(User reporter, PublicDiary reportedDiary);

}
