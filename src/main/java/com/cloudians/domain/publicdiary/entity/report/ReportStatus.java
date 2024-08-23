package com.cloudians.domain.publicdiary.entity.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportStatus {
    PENDING("대기/심사중"),

    GENERAL("일반 컨텐츠"),

    RESTRICTED("신고사유에 적합한 컨텐츠");

    private final String description;
}
