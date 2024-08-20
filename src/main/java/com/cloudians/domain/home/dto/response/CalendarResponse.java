package com.cloudians.domain.home.dto.response;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalendarResponse {
    private Long personalDiaryId;

    private LocalDate date;

    private String elementPhotoUrl;

    private EmotionsResponse emotionsResponse;

    private boolean hasAnswer;

    @Builder
    private CalendarResponse(Long personalDiaryId, LocalDate date, String elementPhotoUrl, EmotionsResponse emotionsResponse, boolean hasAnswer) {
        this.personalDiaryId = personalDiaryId;
        this.date = date;
        this.elementPhotoUrl = elementPhotoUrl;
        this.emotionsResponse = emotionsResponse;
        this.hasAnswer = hasAnswer;
    }

    public static CalendarResponse of(PersonalDiary personalDiary, EmotionsResponse emotionsResponse, boolean hasAnswer) {
        return CalendarResponse.builder()
                .personalDiaryId(personalDiary.getId())
                .date(personalDiary.getDate())
                .elementPhotoUrl(personalDiary.getPersonalDiaryAnalysis().getFiveElement().getElementPhotoUrl())
                .emotionsResponse(emotionsResponse)
                .hasAnswer(hasAnswer)
                .build();
    }
}
