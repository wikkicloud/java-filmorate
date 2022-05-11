package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Long message) {
        super(String.format("Film id: %s not found", message));
    }
}
