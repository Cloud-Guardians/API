package com.cloudians.domain.personaldiary.dto.request;

import lombok.Getter;

@Getter
public class PersonalDiaryEmotionUpdateRequest {

    private Integer joy;

    private Integer sadness;

    private Integer anger;

    private Integer anxiety;

    private Integer boredom;
}