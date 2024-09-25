package com.cloudians.domain.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateReportRequest {
    private List<Long> reportId;
    private String action;

    @Builder
    public UpdateReportRequest(List<Long> reportId, String action) {
        this.reportId = reportId;
        this.action = action;
    }
}

