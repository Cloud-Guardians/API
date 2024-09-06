package com.cloudians.domain.auth.dto.response;

import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwResponse {

    private String to; // 클라우디언스

    private String from; // 받는 이

    private String subject; // 제목

    private String message; // 메시지

    @Builder
    public FindPwResponse(String to, String from, String subject, String message) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    public static FindPwResponse from(User user, String tempPassword) {
        return FindPwResponse.builder()
                .to(user.getUserEmail())
                .from("cloudians12@gmail.com")
                .subject("구르미 그린 그림 임시 비밀번호 안내 메일입니다.")
                .message("구르미 그린 일기의 임시 비밀번호 안내 메일입니다.\n"
                        + "회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해 주세요.\n" + tempPassword)
                .build();
    }

}
