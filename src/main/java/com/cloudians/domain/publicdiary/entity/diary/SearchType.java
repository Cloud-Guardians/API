package com.cloudians.domain.publicdiary.entity.diary;


import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum SearchType {
    TITLE,
    CONTENT,
    AUTHOR,
    TITCONT;

    @JsonCreator
    public static SearchType from(String s) {

        try {
            return SearchType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PublicDiaryException(PublicDiaryExceptionType.WRONG_SEARCH_TYPE);
        }
    }
}

