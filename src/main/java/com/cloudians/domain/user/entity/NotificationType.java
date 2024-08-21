package com.cloudians.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
        DIARY("다이어리"),
        WHISPER("위스퍼"),
        COMMENT("댓글"),
   LIKE("좋아요");
        private final String sender;
   
}
