package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.exception.JsonException;
import com.cloudians.global.exception.JsonExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PersonalDiaryAnalyzeResponse {
    private Long diaryAnalysisId;

    private AnalysisResponse analysis;

    private List<HarmonyTipsResponse> harmonyTips;

    private FortuneResponse tomorrowFortune;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public PersonalDiaryAnalyzeResponse(Long diaryAnalysisId, AnalysisResponse analysis, List<HarmonyTipsResponse> harmonyTips, FortuneResponse tomorrowFortune, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.diaryAnalysisId = diaryAnalysisId;
        this.analysis = analysis;
        this.harmonyTips = harmonyTips;
        this.tomorrowFortune = tomorrowFortune;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PersonalDiaryAnalyzeResponse of(PersonalDiaryAnalysis personalDiaryAnalysis, User user) {
        ObjectMapper objectMapper = new ObjectMapper();

        return PersonalDiaryAnalyzeResponse.builder()
                .diaryAnalysisId(personalDiaryAnalysis.getId())
                .analysis(getAnalysisResponse(personalDiaryAnalysis, objectMapper))
                .harmonyTips(getHarmonyTips(personalDiaryAnalysis.getHarmonyTips(), objectMapper))
                .tomorrowFortune(getFortuneResponse(personalDiaryAnalysis, user))
                .createdAt(personalDiaryAnalysis.getCreatedAt())
                .updatedAt(personalDiaryAnalysis.getUpdatedAt())
                .build();
    }

    private static List<HarmonyTipsResponse> getHarmonyTips(String personalDiaryAnalysis, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(personalDiaryAnalysis, List.class);
        } catch (JsonProcessingException e) {
            throw new JsonException(JsonExceptionType.INVALID_JSON_FORMAT);
        }
    }

    private static FortuneResponse getFortuneResponse(PersonalDiaryAnalysis personalDiaryAnalysis, User user) {
        return FortuneResponse.of(user, personalDiaryAnalysis.getFortuneDetail(), personalDiaryAnalysis.getAdvice());
    }

    private static AnalysisResponse getAnalysisResponse(PersonalDiaryAnalysis personalDiaryAnalysis, ObjectMapper objectMapper) {
        try {
            return AnalysisResponse.of(
                    personalDiaryAnalysis.getFiveElement(),
                    objectMapper.readValue(personalDiaryAnalysis.getElementCharacters(), List.class));
        } catch (JsonProcessingException e) {
            throw new JsonException(JsonExceptionType.INVALID_JSON_FORMAT);
        }
    }
}