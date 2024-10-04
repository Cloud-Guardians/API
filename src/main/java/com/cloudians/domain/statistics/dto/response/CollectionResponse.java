package com.cloudians.domain.statistics.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionResponse {

    private String userEmail;
    private LocalDate date;
    private String mostColorAndElement;
    
}
