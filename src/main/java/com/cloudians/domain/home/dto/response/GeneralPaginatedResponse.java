package com.cloudians.domain.home.dto.response;

import com.cloudians.domain.home.exception.WhisperException;
import com.cloudians.domain.home.exception.WhisperExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class GeneralPaginatedResponse<T> {
    private final boolean hasMore; // reference 타입 ->  기본값이 null임
    private final long lastId;
    private final List<T> elements;

    public static <T, R> GeneralPaginatedResponse<R> of(List<T> data,
                                                        Long count,
                                                        Function<T, Long> getIdFunction,
                                                        Function<T, R> mapFunction) {
        validateDataSize(data);
        boolean hasMore = validateHasMore(count, data);

        List<R> elements = data.stream()
                .map(mapFunction)
                .collect(Collectors.toList());

        Long lastId = getIdFunction.apply(data.get(data.size() - 1));
        return new GeneralPaginatedResponse<>(hasMore, lastId, elements);
    }

    private static <T> boolean validateHasMore(Long count, List<T> data) {
        boolean hasMore = data.size() == count + 1;

        if (hasMore) {
            data.remove(data.size() - 1);
        }
        return hasMore;
    }

    private static <T> void validateDataSize(List<T> data) {
        if (data.isEmpty()) {
            throw new WhisperException(WhisperExceptionType.NO_MORE_DATA);
        }
    }
}