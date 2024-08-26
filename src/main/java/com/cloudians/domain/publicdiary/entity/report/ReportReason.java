package com.cloudians.domain.publicdiary.entity.report;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportReason {

    ADVERTISEMENT_PROMOTION("영리목적/홍보성"),

    COPYRIGHT_INFRINGEMENT("저작권침해"),

    OBSCENITY("음란성/선정성"),

    ABUSE("욕설/인신공격"),

    PERSONAL_INFORMATION_LEAK("개인정보노출"),

    THEFT("도배"),

    OTHER("기타");

    private final java.lang.String description;

    @JsonCreator
    public static ReportReason from(String s) {
        return ReportReason.valueOf(s.toUpperCase());
    }
}