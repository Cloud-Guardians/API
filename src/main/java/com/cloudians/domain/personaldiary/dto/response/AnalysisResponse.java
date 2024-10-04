package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisResponse {
    private String elementName;

    private String elementPhotoName;

    private String fiveElementsPhotoName;

    private List<String> characters;

    private String plusElement;

    private String minusElement;


    @Builder
    private AnalysisResponse(String elementName, String elementPhotoName, String fiveElementsPhotoName, List<String> characters, String plusElement, String minusElement) {
        this.elementName = elementName;
        this.elementPhotoName = elementPhotoName;
        this.fiveElementsPhotoName = fiveElementsPhotoName;
        this.characters = characters;
        this.plusElement = plusElement;
        this.minusElement = minusElement;
    }

    public static AnalysisResponse of(FiveElement element, List<String> characters) {
        return AnalysisResponse.builder()
                .elementName(element.getName())
                .elementPhotoName(element.getElementPhotoName())
                .fiveElementsPhotoName(element.getFiveElementsPhotoName())
                .characters(characters)
                .plusElement(element.getPlusElement())
                .minusElement(element.getMinusElement())
                .build();
    }
}