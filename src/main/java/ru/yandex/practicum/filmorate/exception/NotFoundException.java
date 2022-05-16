package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super(String.format("id %s not found", id));
    }
}
