package com.cloudians.domain.publicdiary.repository.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicDiaryCommentReportRepository {

    boolean existsByReporterAndReportedComment(User reporter, PublicDiaryComment reportedComment);

    void save(PublicDiaryCommentReport publicDiaryCommentReport);


}
