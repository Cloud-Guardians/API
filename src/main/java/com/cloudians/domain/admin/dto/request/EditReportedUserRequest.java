package com.cloudians.domain.admin.dto.request;

import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EditReportedUserRequest {
    private ReportStatus status;

    private int totalReportCount;

    public EditReportedUserRequest(ReportStatus status, int totalReportCount) {
        this.status = status;
        this.totalReportCount = totalReportCount;
    }

}

