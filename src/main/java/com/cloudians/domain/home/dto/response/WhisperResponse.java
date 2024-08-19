package com.cloudians.domain.home.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WhisperResponse {
    private WhisperMessageResponse userMessage;
    private WhisperMessageResponse systemMessage;

    @Builder
    private WhisperResponse(WhisperMessageResponse userMessage, WhisperMessageResponse systemMessage) {
        this.userMessage = userMessage;
        this.systemMessage = systemMessage;
    }


    public static WhisperResponse of(WhisperMessageResponse userChatMessageResponse, WhisperMessageResponse systemChatMessageResponse) {
        return WhisperResponse.builder()
                .userMessage(userChatMessageResponse)
                .systemMessage(systemChatMessageResponse)
                .build();
    }
}
