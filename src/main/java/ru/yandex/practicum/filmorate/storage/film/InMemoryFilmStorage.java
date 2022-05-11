package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static Long globalId = 1L;

    private static Long getNextId() {
        return globalId++;
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", films.get(film.getId()));
        return films.get(film.getId());
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getByID(Long id) {
        return films.get(id);
    }
}
