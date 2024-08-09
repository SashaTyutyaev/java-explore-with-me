package ru.practicum.exceptions;

public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException(String message) {
        super(message);
    }
}
