package com.cloudians.domain.admin.dto.response.comment;

import com.cloudians.domain.admin.dto.response.diary.ReportedDataResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportedCommentResponse {

    private Long reportedCommentId;

    private ReportedDataResponse reportedCommentData;

    private ReportedDataResponse originalPostData;


    @Builder
    public ReportedCommentResponse(Long reportedCommentId, ReportedDataResponse reportedCommentData, ReportedDataResponse originalPostData) {
        this.reportedCommentId = reportedCommentId;
        this.reportedCommentData = reportedCommentData;
        this.originalPostData = originalPostData;
    }

    public static ReportedCommentResponse from(PublicDiaryComment publicDiaryComment) {
        ReportedDataResponse dataResponse = ReportedDataResponse.from1(publicDiaryComment);
        ReportedDataResponse originalPostData = ReportedDataResponse.from2(publicDiaryComment);

        return ReportedCommentResponse.builder()
                .reportedCommentId(publicDiaryComment.getId())
                .reportedCommentData(dataResponse)
                .originalPostData(originalPostData)
                .build();
    }
}
