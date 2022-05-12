package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long message) {
        super(String.format("user id: %s not found", message));
    }
}
