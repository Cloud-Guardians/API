package com.cloudians.domain.personaldiary.exception;

import com.cloudians.global.exception.BaseException;
import com.cloudians.global.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonalDiaryException extends BaseException {

    private final BaseExceptionType exceptionType;

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}