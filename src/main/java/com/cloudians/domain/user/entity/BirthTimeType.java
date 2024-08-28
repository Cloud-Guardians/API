package com.cloudians.domain.user.entity;

import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum BirthTimeType {

    자시,

    축시,

    인시,

    묘시,

    진시,

    사시,

    오시,

    미시,

    신시,

    유시,

    술시,

    해시,

    모름;

    @JsonCreator
    public static BirthTimeType from(String s){
        try{
            return BirthTimeType.valueOf(s.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new UserException(UserExceptionType.WRONG_BIRTHTIME_TYPE);
        }
    }
}
