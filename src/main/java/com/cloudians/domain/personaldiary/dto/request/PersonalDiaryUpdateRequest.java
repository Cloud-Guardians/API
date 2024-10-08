package com.cloudians.domain.personaldiary.dto.request;

import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class PersonalDiaryUpdateRequest {

    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다.")
    private String title;

    private String content;

}