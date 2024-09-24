package com.cloudians.domain.admin.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProcessReportsRequest {
    private String action;

    public ProcessReportsRequest() {}

    @Builder
    public ProcessReportsRequest(String action) {
        this.action = action;
    }
}
