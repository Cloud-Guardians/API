package com.cloudians.domain.personaldiary.entity.analysis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ChatMessage {
    private String role;
    private String content;

    @Builder
    private ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}