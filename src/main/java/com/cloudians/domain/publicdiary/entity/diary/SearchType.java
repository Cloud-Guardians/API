package com.cloudians.domain.publicdiary.entity.diary;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SearchType {
    TITLE,
    CONTENT,
    AUTHOR,
    TITCONT;

    @JsonCreator
    public static SearchType from(String s) {
        return SearchType.valueOf(s.toUpperCase());
    }
}