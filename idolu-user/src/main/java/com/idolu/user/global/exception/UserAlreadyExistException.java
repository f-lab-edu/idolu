package com.idolu.user.global.exception;

public class UserAlreadyExistException extends BaseException {

    public UserAlreadyExistException(ErrorCode code) {
        super(code);
    }
}
