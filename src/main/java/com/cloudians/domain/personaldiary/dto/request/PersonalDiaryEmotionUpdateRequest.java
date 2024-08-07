package com.cloudians.domain.personaldiary.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PersonalDiaryEmotionUpdateRequest {

    private Integer joy;

    private Integer sadness;

    private Integer anger;

    private Integer anxiety;

    private Integer boredom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
