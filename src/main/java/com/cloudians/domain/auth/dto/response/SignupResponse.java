package com.cloudians.domain.auth.dto.response;

import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponse {

 private String userEmail;
 private String nickname;

    @Builder
    private SignupResponse(String userEmail, String nickname) {
        this.userEmail = userEmail;
        this.nickname = nickname;
    }

    static public SignupResponse from(User user) {
        return SignupResponse.builder()
                .userEmail(user.getUserEmail())
                .nickname(user.getNickname())
                .build();

 }

}
