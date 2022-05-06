package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() == null || filmStorage.getByID(film.getId()) == null) {
            log.error("Неизвестный или пустой film id: {}", film.getId());
            throw new ValidationException(String.format("Неизвестный или пустой id: %s", film.getId()));
        }
        validate(film);
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое имя фильма");
            throw new ValidationException("Пустое имя фильма");
        }

        if (film.getDescription().length() > Constants.MAX_SIZE_DESCRIPTION) {
            log.error("Описание фильма не может быть больше {}", Constants.MAX_SIZE_DESCRIPTION);
            throw new ValidationException("Описание фильма не может быть больше" + Constants.MAX_SIZE_DESCRIPTION);
        }

        if (film.getReleaseDate().isBefore(Constants.MIN_DATE_RELEASE)) {
            log.error("Дата релиза фильма {} это раньше {}",film.getReleaseDate(), Constants.MIN_DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма раньше " + Constants.MIN_DATE_RELEASE);
        }

        if (film.getDuration().toSeconds() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }
    }
}
