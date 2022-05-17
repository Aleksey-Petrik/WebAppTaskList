package ru.tasklist.springboot.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class RoleNotFoundException extends AuthenticationException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
