package com.cloudians.domain.publicdiary.dto.request;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequest {
    private String reason;

    private String customReason;

    @Builder
    private ReportRequest(String reason, String customReason) {
        this.reason = reason;
        this.customReason = customReason;
    }

    public PublicDiaryReport toDiaryReport(User user, PublicDiary publicDiary) {
        ReportReason reasonEnum = ReportReason.from(reason);

        return PublicDiaryReport.builder()
                .reportedDiary(publicDiary)
                .reporter(user)
                .reason(reasonEnum)
                .customReason(customReason)
                .status(ReportStatus.PENDING)
                .build();
    }

    public PublicDiaryCommentReport toCommentReport(User user, PublicDiaryComment publicDiaryComment) {
        ReportReason reasonEnum = ReportReason.from(reason);

        return PublicDiaryCommentReport.builder()
                .reportedComment(publicDiaryComment)
                .reporter(user)
                .reason(reasonEnum)
                .customReason(customReason)
                .status(ReportStatus.PENDING)
                .build();
    }
}