package com.cloudians.domain.publicdiary.dto.response.report;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PublicDiaryCommentReportResponse {

    private Long reportId;

    private UserProfileResponse reporter;

    private Long reportedCommentId;

    private ReportReason reason;

    private String customReason;

    private ReportStatus status;

    private boolean isRead;

    @Builder
    private PublicDiaryCommentReportResponse(Long reportId, UserProfileResponse reporter, Long reportedCommentId, ReportReason reason, String customReason, ReportStatus status, boolean isRead) {
        this.reportId = reportId;
        this.reporter = reporter;
        this.reportedCommentId = reportedCommentId;
        this.reason = reason;
        this.customReason = customReason;
        this.status = status;
        this.isRead = isRead;
    }

    public static PublicDiaryCommentReportResponse of(PublicDiaryCommentReport report, User reporter, PublicDiaryComment reportedComment) {
        return PublicDiaryCommentReportResponse.builder()
                .reportId(report.getId())
                .reporter(UserProfileResponse.from(reporter))
                .reportedCommentId(reportedComment.getId())
                .reason(report.getReason())
                .customReason(report.getCustomReason())
                .status(report.getStatus())
                .isRead(report.isRead())
                .build();
    }
}
