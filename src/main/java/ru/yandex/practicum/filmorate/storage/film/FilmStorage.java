package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film create(Film film);

    public Film update(Film film);

    public Film getByID(Integer id);

    public List<Film> findAll();

    public void remove(Integer id);

}
