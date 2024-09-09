package com.cloudians.domain.user.entity;

import com.cloudians.domain.user.dto.request.UserProfileRequest;
import com.cloudians.domain.user.dto.request.UserRequest;
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

    public User edit(UserRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }

        if (request.getGender() != '\0') {
            this.gender = request.getGender();
        }
        if (request.getBirthdate() != null) {
            this.birthdate = request.getBirthdate();
        }
        if (request.getBirthTime() != null) {
            this.birthTime = BirthTimeType.from(request.getBirthTime());
        }
        if (request.getCalendarType() != null) {
            this.calendarType = CalendarType.from(request.getCalendarType());
        }

        return this;
    }

    public User profileEdit(UserProfileRequest request, User user) {

        if (request.getProfileUrl() != null) {
            user.profileUrl = request.getProfileUrl();
        }
        if (request.getNickname() != null) {
            user.nickname = request.getNickname();
        }

        return this;
    }


}