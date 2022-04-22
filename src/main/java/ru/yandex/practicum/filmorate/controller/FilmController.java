package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    public static final int MAX_SIZE_DESCRIPTION = 200;
    public static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1985, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    private Integer generateId() {
        return id++;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое имя фильма");
            throw new ValidationException("Пустое имя фильма");
        }

        if (film.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            log.error("Описание фильма не может быть больше {}", MAX_SIZE_DESCRIPTION);
            throw new ValidationException("Описание фильма не может быть больше" + MAX_SIZE_DESCRIPTION);
        }

        if (film.getReleaseDate().isAfter(MIN_DATE_RELEASE)) {
            log.error("Дата релиза фильма раньше {}", MIN_DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма раньше " + MIN_DATE_RELEASE);
        }

        if (film.getDuration().toSeconds() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
       if (film.getId() == null || !films.containsKey(film.getId())) {
           log.error("Неизвестный или пустой film id: {}", film.getId());
           throw new ValidationException("Неизвестный или пустой id: " + film.getId());
       }
       films.put(film.getId(), film);
       return film;
    }
}
