package com.cloudians.domain.admin.dto.response.diary;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static com.cloudians.domain.personaldiary.entity.QPersonalDiary.personalDiary;
import static com.cloudians.domain.publicdiary.entity.comment.QPublicDiaryComment.publicDiaryComment;

@Getter
public class ReportedDataResponse {
    private Long id;

    private ReportedUserResponse reportedUser;

    private String title;

    private String content;

    private String photoUrl;

    private LocalDate date;

    @Builder
    public ReportedDataResponse(Long id, ReportedUserResponse reportedUser, String title, String content, String photoUrl, LocalDate date) {
        this.id = id;
        this.reportedUser = reportedUser;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.date = date;
    }

    public static ReportedDataResponse from(PersonalDiary personalDiary) {
        return ReportedDataResponse.builder()
                .id(personalDiary.getId())
                .reportedUser(ReportedUserResponse.from(personalDiary.getUser()))
                .title(personalDiary.getTitle())
                .content(personalDiary.getContent())
                .photoUrl(personalDiary.getPhotoUrl())
                .date(personalDiary.getDate())
                .build();
    }

    public static ReportedDataResponse from1(PublicDiaryComment publicDiaryComment){
        return ReportedDataResponse.builder()
                .id(publicDiaryComment.getId())
                .reportedUser(ReportedUserResponse.from(publicDiaryComment.getAuthor()))
                .content(publicDiaryComment.getContent())
                .build();
    }

    public static ReportedDataResponse from2(PublicDiaryComment publicDiaryComment){
           PublicDiary publicDiary = publicDiaryComment.getPublicDiary();
        return ReportedDataResponse.builder()
                .id(publicDiary.getId())
                .title(publicDiary.getPersonalDiary().getTitle())
                .content(publicDiary.getPersonalDiary().getContent())
                .photoUrl(publicDiary.getPersonalDiary().getPhotoUrl())
                .build();
    }
}
