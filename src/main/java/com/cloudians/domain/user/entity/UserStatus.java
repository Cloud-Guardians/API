package com.cloudians.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    ADMIN("관리자"),

    DEFAULT("일반유저"),

    BLOCKED("차단된 유저");

    private String description;
}