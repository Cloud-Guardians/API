package com.cloudians.domain.user.exception;

import com.cloudians.global.exception.BaseException;
import com.cloudians.global.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserException extends BaseException {

    private final BaseExceptionType exceptionType;

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
