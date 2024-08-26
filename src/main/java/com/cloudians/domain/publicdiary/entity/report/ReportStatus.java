package com.cloudians.domain.publicdiary.entity.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportStatus {

    PENDING("대기/심사중"),

    DISMISS("신고기각");
    
    private final String description;
}