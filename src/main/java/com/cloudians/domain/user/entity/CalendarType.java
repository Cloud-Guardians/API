package com.cloudians.domain.user.entity;

import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum CalendarType {

    SOLAR,

    LUNAR;

    @JsonCreator
    public static CalendarType from(String s){
        try{
            return CalendarType.valueOf(s.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new UserException(UserExceptionType.WRONG_CALENDAR_TYPE);
        }
    }
}