package com.cloudians.global;

import lombok.Data;

@Data
public class Message {
    private Object data;
    private String error;
    private Integer statusCode;

    public Message(Object data, Integer statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public Message(String error, Integer statusCode) {
        this.error = error;
        this.statusCode = statusCode;
    }
    
    public Message(Object data, String error, Integer statusCode) {
	this.data=data;
	this.error=error;
	this.statusCode=statusCode;
    }
}