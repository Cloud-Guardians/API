package com.cloudians.domain.admin.dto.response.comment;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminReportCommentResponse {

    private Long commentReportId;

    private UserProfileResponse reporter;

    private ReportedCommentResponse reportedComment;

    private ReportReason reason;

    private String customReason;

    private ReportStatus status;

    private boolean isRead;

    @Builder
    public AdminReportCommentResponse(Long commentReportId, UserProfileResponse reporter, ReportedCommentResponse reportedComment, ReportReason reason, String customReason, ReportStatus status, boolean isRead) {
        this.commentReportId = commentReportId;
        this.reporter = reporter;
        this.reportedComment = reportedComment;
        this.reason = reason;
        this.customReason = customReason;
        this.status = status;
        this.isRead = isRead;
    }

    public static AdminReportCommentResponse of(PublicDiaryCommentReport publicDiaryCommentReport, User reporter) {
        PublicDiaryComment publicDiaryComment = publicDiaryCommentReport.getReportedComment();

        return AdminReportCommentResponse.builder()
                .commentReportId(publicDiaryCommentReport.getId())
                .reporter(UserProfileResponse.from(reporter))
                .reportedComment(ReportedCommentResponse.from(publicDiaryComment))
                .reason(publicDiaryCommentReport.getReason())
                .customReason(publicDiaryCommentReport.getCustomReason())
                .status(publicDiaryCommentReport.getStatus())
                .isRead(publicDiaryCommentReport.isRead())
                .build();
    }
}
