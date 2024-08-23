package com.cloudians.domain.publicdiary.dto.response;

import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserReportResponse {
    private String userEmail;

    private String nickname;

    private String profilePhotoUrl;

    @Builder
    private UserReportResponse(String userEmail, String nickname, String profilePhotoUrl) {
        this.userEmail = userEmail;
        this.nickname = nickname;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public static UserReportResponse from(User user) {
        return UserReportResponse.builder()
                .userEmail(user.getUserEmail())
                .nickname(user.getNickname())
                .profilePhotoUrl(user.getProfileUrl())
                .build();
    }
}
