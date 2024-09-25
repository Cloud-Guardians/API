package com.cloudians.domain.admin.dto.response.diary;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportedDiaryResponse {

    private Long reportedDiaryId;

    private ReportedDataResponse reportedDiaryData;

    private String reportedDiaryViews;

    @Builder
    public ReportedDiaryResponse(Long reportedDiaryId, ReportedDataResponse reportedDiaryData) {
        this.reportedDiaryId = reportedDiaryId;
        this.reportedDiaryData = reportedDiaryData;
    }

    public static ReportedDiaryResponse from(PublicDiary publicDiary) {
        ReportedDataResponse dataResponse = ReportedDataResponse.from(publicDiary.getPersonalDiary());

        return ReportedDiaryResponse.builder()
                .reportedDiaryId(publicDiary.getId())
                .reportedDiaryData(dataResponse)
                .build();
    }
}
