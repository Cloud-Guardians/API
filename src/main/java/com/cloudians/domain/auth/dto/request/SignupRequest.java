package com.cloudians.domain.auth.dto.request;

import java.time.LocalDate;

import com.cloudians.domain.user.entity.BirthTimeType;
import com.cloudians.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    private String userEmail;
    private String password;
    private String name;
    private String nickname;
    private char gender;
    private LocalDate birthdate;
    private String birthTime;

    //TODO: 각각 양식 넣어서 제한 두고 not null 안 들어오게 어노테이션 생성
    @Builder
    private SignupRequest(String userEmail, String password, String name, char gender, LocalDate birthdate, String birthTime) {
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
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthdate(birthdate)
                .birthTime(birthTime)
                .build();
    }
}