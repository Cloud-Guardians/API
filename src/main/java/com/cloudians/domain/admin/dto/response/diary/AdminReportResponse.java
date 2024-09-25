package com.cloudians.domain.admin.dto.response.diary;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminReportResponse {
    private Long reportId;

    private UserProfileResponse reporter;

    private ReportedDiaryResponse reportedDiary;

    private ReportReason reason;

    private String customReason;

    private ReportStatus status;

    private boolean isRead;

    @Builder
    public AdminReportResponse(Long reportId, UserProfileResponse reporter, ReportedDiaryResponse reportedDiary,
                               ReportReason reason, String customReason,
                               ReportStatus status, boolean isRead) {
        this.reportId = reportId;
        this.reporter = reporter;
        this.reportedDiary = reportedDiary;
        this.reason = reason;
        this.customReason = customReason;
        this.status = status;
        this.isRead = isRead;
    }

    public static AdminReportResponse of(PublicDiaryReport publicDiaryReport, User reporter) {
        PublicDiary publicDiary = publicDiaryReport.getReportedDiary();

        return AdminReportResponse.builder()
                .reportId(publicDiaryReport.getId())
                .reporter(UserProfileResponse.from(reporter))
                .reportedDiary(ReportedDiaryResponse.from(publicDiary))
                .reason(publicDiaryReport.getReason())
                .customReason(publicDiaryReport.getCustomReason())
                .status(publicDiaryReport.getStatus())
                .isRead(publicDiaryReport.isRead())
                .build();
    }


}
