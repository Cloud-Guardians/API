package com.cloudians.domain.publicdiary.entity;

import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchCondition {
    String content;

    SearchType type;

    @Builder
    private SearchCondition(String content, SearchType type) {
        this.content = content;
        this.type = type;
    }

    public static SearchCondition of(String searchType, String keyword) {
        return SearchCondition.builder()
                .type(getType(searchType))
                .content(keyword)
                .build();
    }

    private static SearchType getType(String searchType) {
        if (SearchType.hasType(searchType)) {
            return SearchType.valueOf(searchType);
        }
        throw new PublicDiaryException(PublicDiaryExceptionType.WRONG_SEARCH_TYPE);
    }
}
