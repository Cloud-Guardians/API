package com.cloudians.domain.auth.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class ResetPwRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message = "이메일 양식을 확인해 주세요.")
    private String userEmail;

    @NotBlank(message = "기존 비밀번호를 입력해 주세요.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 8-16자로 설정해 주세요.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인을 입력해 주세요.")
    private String confirmPassword;

    @Builder
    public ResetPwRequest(String userEmail, String oldPassword, String newPassword, String confirmPassword) {
        this.userEmail = userEmail;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}

