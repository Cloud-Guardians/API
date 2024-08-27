package com.cloudians.domain.publicdiary.entity.diary;

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
                .type(SearchType.from(searchType))
                .content(keyword)
                .build();
    }
}
