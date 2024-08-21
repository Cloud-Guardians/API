package com.cloudians.domain.publicdiary.entity.diary;

import java.util.Arrays;

public enum SearchType {
    TITLE,
    CONTENT,
    AUTHOR,
    TITCONT;

    public static boolean hasType(String string) {
        return Arrays.stream(SearchType.values())
                .anyMatch(type -> type.name().equals(string));
    }
}
