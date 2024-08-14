package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.ChatGptRequest;
import com.cloudians.domain.personaldiary.dto.response.ChatGptResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.analysis.ChatMessage;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final RestTemplate restTemplate;

    @Value("${chatgpt.multi.url}")
    private String apiUrl;

    @Value("${chatgpt.multi.model}")
    private String model;

    @Value("${chatgpt.max-tokens}")
    private int maxToken;

    @Value("${chatgpt.temperature}")
    private double temperature;

    public String askQuestion(PersonalDiary personalDiary, User user) throws Exception{
        String userPrompt = getUserPrompt(personalDiary);
        String systemPrompt = getSystemPrompt(user);


        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.builder()
                .role("user")
                .content(userPrompt)
                .build());
        messages.add(ChatMessage.builder()
                .role("system")
                .content(systemPrompt)
                .build());

        ChatGptRequest request = ChatGptRequest.builder()
                .model(model)
                .maxTokens(maxToken)
                .temperature(temperature)
                .messages(messages)
                .build();

        ChatGptResponse response = restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("Invalid response from ChatGPT API");
        }
        for (ChatGptResponse.Choice choice : response.getChoices()) {
            if (choice.getMessage() == null) {
                throw new RuntimeException("text null");
            }
        }
        return response.getChoices().get(0).getMessage().getContent();
    }

    private String getUserPrompt(PersonalDiary personalDiary) {
        String title = personalDiary.getTitle();
        String content = personalDiary.getContent();

        String text = "일기제목: %s\n" +
                "내용: %s\n";

        return String.format(text, title, content);
    }

     private String getSystemPrompt(User user) {
        Date birthdate = user.getBirthdate();
        String birthTime = user.getBirthTime();

        String text = "일기 내용을 바탕으로 음양오행 요소와 그 요소의 영향을 받는 상황에서 생일:%s, 생시:%s인 사람의 내일의 운세 및 조언을 알려주세요. 다음 형식으로 답변해 주세요.\n" +
                "요소: [화,수,목,금,토 중 하나만]\n" +
                "총운: [3-4문장으로 내일의 운세 설명]\n" +
                "조언: [3-4문장으로 주어질 조언]\n";

         return String.format(text, birthdate, birthTime);
    }
}
