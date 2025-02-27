package com.example.redcross.exception;

import lombok.Data;

@Data
public class MessageException extends RuntimeException {
    private String msg;

    public MessageException(String msg) {
        this.msg = msg;
    }
}
