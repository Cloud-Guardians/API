package com.cloudians.domain.home.dto.response;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WhisperMessageResponse {
    private Long whisperMessageId;

    private String userEmail;

    private String content;

    private LocalDateTime timestamp;

    private SenderType sender;

    @Builder
    private WhisperMessageResponse(Long whisperMessageId, String userEmail, String content, LocalDateTime timestamp, SenderType sender) {
        this.whisperMessageId = whisperMessageId;
        this.userEmail = userEmail;
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public static WhisperMessageResponse of(WhisperMessage whisperMessage) {
        return WhisperMessageResponse.builder()
                .whisperMessageId(whisperMessage.getId())
                .userEmail(whisperMessage.getUser().getUserEmail())
                .content(whisperMessage.getMessage())
                .timestamp(whisperMessage.getTimestamp())
                .sender(whisperMessage.getSender())
                .build();
    }
}