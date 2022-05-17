package ru.tasklist.springboot.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class UserOrEmailAlreadyException extends AuthenticationException {
    public UserOrEmailAlreadyException(String message) {
        super(message);
    }
    public UserOrEmailAlreadyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
