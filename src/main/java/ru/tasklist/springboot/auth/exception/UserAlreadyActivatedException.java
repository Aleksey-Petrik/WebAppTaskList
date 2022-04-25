package ru.tasklist.springboot.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyActivatedException extends AuthenticationException {
    public UserAlreadyActivatedException(String message) {
        super(message);
    }

    public UserAlreadyActivatedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
