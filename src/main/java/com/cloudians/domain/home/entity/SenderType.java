package com.cloudians.domain.home.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SenderType {

    USER("유저"),
    SYSTEM("시스템");

    private final String sender;
}