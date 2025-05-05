package com.idolu.user.global.exception;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(ErrorCode code) {
        super(code);
    }
}
