package com.cloudians.domain.personaldiary.dto.request;

import com.cloudians.domain.personaldiary.entity.analysis.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatGptRequest {

    private String model;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature;

    private List<ChatMessage> messages;

    @Builder
    private ChatGptRequest(String model, Integer maxTokens, Double temperature, String userPrompt, String systemPrompt) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        getMessages(userPrompt, systemPrompt);
    }

    private void getMessages(String userPrompt, String systemPrompt) {
        this.messages = new ArrayList<>();

        this.messages.add(ChatMessage.builder()
                .role("user")
                .content(userPrompt)
                .build());

        this.messages.add(ChatMessage.builder()
                .role("system")
                .content(systemPrompt)
                .build());
    }
}