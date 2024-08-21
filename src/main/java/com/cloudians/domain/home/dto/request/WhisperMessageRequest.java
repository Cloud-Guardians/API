package com.cloudians.domain.home.dto.request;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
public class WhisperMessageRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    @ConstructorProperties(value = "{content}")
    private WhisperMessageRequest(String content) {
        this.content = content;
    }

    public WhisperMessage toEntity(User user, String message, LocalDateTime timestamp, SenderType sender) {
        return WhisperMessage.builder()
                .user(user)
                .message(message)
                .timestamp(timestamp)
                .sender(sender)
                .build();
    }
}