package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PersonalDiaryAnalyzeResponse {
    private Long diaryAnalysisId;

    private AnalysisResponse analysis;

    private List<HarmonyTipsResponse> harmonyTips;

    private FortuneResponse tomorrowFortune;

    @Builder
    public PersonalDiaryAnalyzeResponse(Long diaryAnalysisId, AnalysisResponse analysis, List<HarmonyTipsResponse> harmonyTips, FortuneResponse tomorrowFortune) {
        this.diaryAnalysisId = diaryAnalysisId;
        this.analysis = analysis;
        this.harmonyTips = harmonyTips;
        this.tomorrowFortune = tomorrowFortune;
    }

    public static PersonalDiaryAnalyzeResponse of(PersonalDiaryAnalysis personalDiaryAnalysis, User user) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return PersonalDiaryAnalyzeResponse.builder()
                .diaryAnalysisId(personalDiaryAnalysis.getId())
                .analysis(AnalysisResponse.of(
                        personalDiaryAnalysis.getFiveElement(),
                        objectMapper.readValue(personalDiaryAnalysis.getElementCharacters(), List.class)))
                .harmonyTips(objectMapper.readValue(personalDiaryAnalysis.getHarmonyTips(), List.class))
                .tomorrowFortune(FortuneResponse.of(user, personalDiaryAnalysis.getFortuneDetail(), personalDiaryAnalysis.getAdvice()))
                .build();
    }
}
