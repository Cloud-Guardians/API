package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublicDiaryCommentReportRepositoryImpl implements PublicDiaryCommentReportRepository {

    private final PublicDiaryCommentReportJpaRepository publicDiaryCommentReportJpaRepository;

    @Override
    public boolean existsByReporterAndReportedComment(User reporter, PublicDiaryComment reportedComment) {
        return publicDiaryCommentReportJpaRepository.existsByReporterAndReportedComment(reporter, reportedComment);
    }

    @Override
    public void save(PublicDiaryCommentReport publicDiaryCommentReport) {
        publicDiaryCommentReportJpaRepository.save(publicDiaryCommentReport);
    }
}
