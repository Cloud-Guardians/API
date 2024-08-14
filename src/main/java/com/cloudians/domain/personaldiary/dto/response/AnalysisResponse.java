package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElementCharacter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisResponse {
    private String elementName;

    private List<String> characters;

    private String plusElement;

    private String minusElement;

    @Builder
    private AnalysisResponse(String elementName, List<String> characters, String plusElement, String minusElement) {
        this.elementName = elementName;
        this.characters = characters;
        this.plusElement = plusElement;
        this.minusElement = minusElement;
    }

    public static AnalysisResponse of(FiveElement element, List<String> characters) {
        return AnalysisResponse.builder()
                .elementName(element.getName())
                .characters(characters)
                .plusElement(element.getPlusElement())
                .minusElement(element.getMinusElement())
                .build();
    }
}
