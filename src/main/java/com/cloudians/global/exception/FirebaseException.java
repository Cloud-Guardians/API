package com.cloudians.global.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirebaseException extends BaseException{
    
    private final BaseExceptionType exceptionType;

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }

}
