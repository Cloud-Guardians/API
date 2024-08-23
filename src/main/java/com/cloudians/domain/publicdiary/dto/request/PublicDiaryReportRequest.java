package com.cloudians.domain.publicdiary.dto.request;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportReason;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicDiaryReportRequest {
    private String reason;

    private String customReason;

    @Builder
    private PublicDiaryReportRequest(String reason, String customReason) {
        this.reason = reason;
        this.customReason = customReason;
    }

    public PublicDiaryReport toEntity(User user, PublicDiary publicDiary) {
        ReportReason reasonEnum = ReportReason.from(reason);

        return PublicDiaryReport.builder()
                .reportedDiary(publicDiary)
                .reporter(user)
                .reason(reasonEnum)
                .customReason(customReason)
                .build();
    }
}