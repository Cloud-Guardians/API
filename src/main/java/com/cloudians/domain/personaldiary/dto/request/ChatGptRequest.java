package com.cloudians.domain.personaldiary.dto.request;

import com.cloudians.domain.personaldiary.entity.analysis.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatGptRequest {

    private String model;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature;

    private List<ChatMessage> messages;

    @Builder
    private ChatGptRequest(String model, Integer maxTokens, Double temperature, List<ChatMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.messages = messages;
    }
}
