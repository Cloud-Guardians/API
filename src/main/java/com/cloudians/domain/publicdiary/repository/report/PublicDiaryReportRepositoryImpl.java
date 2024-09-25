package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublicDiaryReportRepositoryImpl implements PublicDiaryReportRepository {

    private final PublicDiaryReportJpaRepository publicDiaryReportJpaRepository;

    @Override
    public void save(PublicDiaryReport publicDiaryReport) {
        publicDiaryReportJpaRepository.save(publicDiaryReport);
    }

    @Override
    public boolean existsByReporterAndReportedDiary(User reporter, PublicDiary reportedDiary) {
        return publicDiaryReportJpaRepository.existsByReporterAndReportedDiary(reporter, reportedDiary);
    }
}
