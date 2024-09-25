package com.cloudians.domain.admin.exception;

import com.cloudians.global.exception.BaseException;
import com.cloudians.global.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminException extends BaseException {

    private final BaseExceptionType exceptionType;

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}

