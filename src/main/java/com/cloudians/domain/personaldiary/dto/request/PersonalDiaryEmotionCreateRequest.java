package com.cloudians.domain.personaldiary.dto.request;

import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Builder
    private PersonalDiaryEmotionCreateRequest(int joy, int sadness, int anger, int anxiety, int boredom) {
        this.joy = joy;
        this.sadness = sadness;
        this.anger = anger;
        this.anxiety = anxiety;
        this.boredom = boredom;
    }

    public PersonalDiaryEmotion toEntity() {
        return PersonalDiaryEmotion.builder()
                .joy(joy)
                .sadness(sadness)
                .anger(anger)
                .anxiety(anxiety)
                .boredom(boredom)
                .build();
    }
}
