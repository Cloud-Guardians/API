package com.cloudians.domain.auth.dto.request;

import com.cloudians.domain.user.entity.BirthTimeType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message = "이메일 양식을 확인해 주세요.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String userEmail;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8-16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @Pattern(regexp = "^[가-힣]*$", message = "사용자 이름은 한글만 입력할 수 있습니다.")
    @Size(max = 10)
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotNull(message = "성별을 입력해 주세요.")
    private char gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생년월일을 입력해 주세요.")
    @Past
    private LocalDate birthdate;

    @NotNull(message = "생시를 입력해 주세요.")
    private String birthTime;

    //TODO: 각각 양식 넣어서 제한 두고 not null 안 들어오게 어노테이션 생성
    public SignupRequest(String userEmail, String password, String name, char gender, LocalDate birthdate, String birthTime) {
        this.userEmail = userEmail;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.birthTime = birthTime;
    }

    public User toUser(String encodedPassword, String nickname) {

        BirthTimeType birthTime = BirthTimeType.from(this.birthTime);

        return User.builder()
                .userEmail(userEmail)
                .password(encodedPassword)
                .nickname(nickname)
                .name(name)
                .gender(validatedGender(gender))
                .birthdate(birthdate)
                .birthTime(birthTime)
                .build();
    }

    private char validatedGender(char gender){
        if(gender != 'w' && gender != 'm'){
            throw new UserException(UserExceptionType.USER_GENDER_INVALID);
        }
        return gender;
    }
}
