package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Long message) {
        super(String.format("film id: %s not found", message));
    }
}
