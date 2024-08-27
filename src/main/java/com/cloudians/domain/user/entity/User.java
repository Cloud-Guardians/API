package com.cloudians.domain.user.entity;

import com.cloudians.domain.auth.entity.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;


@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(STRING)
    private SignupType signupType;

    private String name;

    @Column(unique = true)
    private String nickname;

    private String password;

    private char gender;

    @Column(name = "profile_url")
    private String profileUrl;

    @Enumerated(STRING)
    @Column(name = "calendar_type")
    private CalendarType calendarType;

    private LocalDate birthdate;

    @Enumerated(STRING)
    @Column(name = "birth_time")
    private BirthTimeType birthTime;

    @Column(name = "total_report_count")
    private int totalReportCount;

    // TODO: 관리자, 일반, 차단 enum
    @Enumerated(STRING)
    private UserStatus status;

    @Builder
    private User(String userEmail, SignupType signupType, String name, String nickname, String password, char gender, String profileUrl, CalendarType calendarType, LocalDate birthdate, BirthTimeType birthTime, int totalReportCount, UserStatus status) {
        this.userEmail = userEmail;
        this.signupType = signupType;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.gender = gender;
        this.profileUrl = profileUrl;
        this.calendarType = calendarType;
        this.birthdate = birthdate;
        this.birthTime = birthTime;
        this.totalReportCount = 0;
        this.status = UserStatus.DEFAULT;
    }

    //  Convert User entity to UserResponse DTO
//    public UserResponse toDto() {
//        return UserResponse.builder()
//                .userEmail(this.userEmail)
//                .signupType(this.signupType)
//                .name(this.name)
//                .nickname(this.nickname)
//                .gender(this.gender)
//                .profileUrl(this.profileUrl)
//                .calendarType(this.calendarType)
//                .birthdate(this.birthdate)
//                .birthTime(this.birthTime)
//                .totalReportCount(this.totalReportCount)
//                .status(this.status)
//                .build();
//    }


}