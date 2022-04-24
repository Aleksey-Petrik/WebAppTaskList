package ru.tasklist.springboot.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JsonException {
    private final String message;
}
