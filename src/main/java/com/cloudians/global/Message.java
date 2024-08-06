package com.cloudians.global;

import lombok.Data;

@Data
public class Message {
    private Object data;
    private String errorMessage;
    private Integer statusCode;

    public Message(Object data, Integer statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public Message(String errorMessage, Integer statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}