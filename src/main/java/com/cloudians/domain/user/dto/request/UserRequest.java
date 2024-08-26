package com.cloudians.domain.user.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserRequest { // 이름 UpdateUserInfoRequest가 좋을 것 같아요!

    //    private String userEmail;
//    private int signupType;
//    private String password;
    private String name;
    //    private String profileUrl;
//    private String nickname;
    private char gender;
    private String calendarType;
    private LocalDate birthdate;
    private String birthTime;
//    private int status;

    public UserRequest() {
    }
}