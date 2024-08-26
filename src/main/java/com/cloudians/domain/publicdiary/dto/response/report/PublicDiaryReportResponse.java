package com.cloudians.domain.publicdiary.dto.response.report;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PublicDiaryReportResponse {

    private Long reportId;

    private UserProfileResponse reporter;

    private Long reportedDiaryId;

    private ReportReason reason;

    private String customReason;

    private ReportStatus status;

    private boolean isRead;

    @Builder
    private PublicDiaryReportResponse(Long reportId, UserProfileResponse reporter, Long reportedDiaryId, ReportReason reason, java.lang.String customReason, ReportStatus status, boolean isRead) {
        this.reportId = reportId;
        this.reporter = reporter;
        this.reportedDiaryId = reportedDiaryId;
        this.reason = reason;
        this.customReason = customReason;
        this.status = status;
        this.isRead = isRead;
    }

    public static PublicDiaryReportResponse of(PublicDiaryReport report, User reporter, PublicDiary reportedDiary) {
        return PublicDiaryReportResponse.builder()
                .reportId(report.getId())
                .reporter(UserProfileResponse.from(reporter))
                .reportedDiaryId(reportedDiary.getId())
                .reason(report.getReason())
                .customReason(report.getCustomReason())
                .status(report.getStatus())
                .isRead(report.isRead())
                .build();
    }
}