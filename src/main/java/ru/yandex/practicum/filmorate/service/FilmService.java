package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

@Slf4j
@Service
public class FilmService extends GenericService<Film> {

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") Storage<Film> storage) {
        super(storage);
    }

    @Override
    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое имя фильма");
            throw new ValidationException("Пустое имя фильма");
        }
        if (film.getMpa() == null) {
            log.error("Не заполнен рейтинг MPA");
            throw new ValidationException("Не заполнен рейтинг MPA");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Пустое описание фильма");
            throw new ValidationException("Пустое описание фильма");
        }

        if (film.getDescription().length() > Constants.MAX_SIZE_DESCRIPTION) {
            log.error("Описание фильма не может быть больше {}", Constants.MAX_SIZE_DESCRIPTION);
            throw new ValidationException("Описание фильма не может быть больше" + Constants.MAX_SIZE_DESCRIPTION);
        }

        if (film.getReleaseDate().isBefore(Constants.MIN_DATE_RELEASE)) {
            log.error("Дата релиза фильма {} это раньше {}", film.getReleaseDate(), Constants.MIN_DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма раньше " + Constants.MIN_DATE_RELEASE);
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }
    }
}
