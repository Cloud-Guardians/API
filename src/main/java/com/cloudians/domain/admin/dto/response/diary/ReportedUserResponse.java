package com.cloudians.domain.admin.dto.response.diary;

import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportedUserResponse {
    private String userEmail;

    private String name;

    private String nickname;

    private String profileUrl;

    private int totalReportCount;

    private UserStatus status;


    @Builder
    public ReportedUserResponse(String userEmail, String name, String nickname, String profileUrl, int totalReportCount, UserStatus status) {
        this.userEmail = userEmail;
        this.name = name;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.totalReportCount = totalReportCount;
        this.status = status;
    }

    public static ReportedUserResponse from(User user) {
        return ReportedUserResponse.builder()
                .userEmail(user.getUserEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .totalReportCount(user.getTotalReportCount())
                .status(user.getStatus())
                .build();
    }

    public static ReportedUserResponse edit(User user) {
        return ReportedUserResponse.builder()
                .status(user.getStatus())
                .totalReportCount(user.getTotalReportCount())
                .build();
    }
}
