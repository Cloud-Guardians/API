package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class FortuneResponse {
    private LocalDate birthdate;

    private String name;

    private String fortuneDetail;

    private String advice;

    @Builder
    private FortuneResponse(LocalDate birthdate, String name, String fortuneDetail, String advice) {
        this.birthdate = birthdate;
        this.name = name;
        this.fortuneDetail = fortuneDetail;
        this.advice = advice;
    }

    public static FortuneResponse of(User user, String fortuneDetail, String advice) {
        return FortuneResponse.builder()
                .birthdate(user.getBirthdate())
                .name(user.getName())
                .fortuneDetail(fortuneDetail)
                .advice(advice)
                .build();
    }
}