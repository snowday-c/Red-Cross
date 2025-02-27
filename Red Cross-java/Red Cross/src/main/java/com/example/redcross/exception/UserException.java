package com.example.redcross.exception;

import lombok.Data;

@Data
public class UserException extends RuntimeException {

    private String msg;

    public UserException(String msg) {
        this.msg = msg;
    }


}