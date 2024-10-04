package com.cloudians.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class TempPwRequest {
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message = "이메일 양식을 확인해 주세요.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String userEmail;

    private String tempPw;

    public TempPwRequest(String userEmail, String tempPw) {
        this.userEmail = userEmail;
        this.tempPw = tempPw;
    }
}
