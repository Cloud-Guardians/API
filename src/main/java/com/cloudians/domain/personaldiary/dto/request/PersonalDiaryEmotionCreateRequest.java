package com.cloudians.domain.personaldiary.dto.request;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class PersonalDiaryEmotionCreateRequest {

    @Min(value = 0, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    @Max(value = 100, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    private int joy;

    @Min(value = 0, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    @Max(value = 100, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    private int sadness;

    @Min(value = 0, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    @Max(value = 100, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    private int anger;

    @Min(value = 0, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    @Max(value = 100, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    private int anxiety;

    @Min(value = 0, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    @Max(value = 100, message = "최소 0에서 최대 100사이의 값을 입력해주세요.")
    private int boredom;

    @NotNull(message = "날짜를 입력해주세요.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    private PersonalDiaryEmotionCreateRequest(int joy, int sadness, int anger, int anxiety, int boredom, LocalDate date) {
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
        this.date = date;
    }

    public PersonalDiaryEmotion toEntity(User user) {
        return PersonalDiaryEmotion.builder()
                .user(user)
                .joy(joy)
                .sadness(sadness)
                .anger(anger)
                .anxiety(anxiety)
                .boredom(boredom)
                .date(date)
                .build();
    }
}
