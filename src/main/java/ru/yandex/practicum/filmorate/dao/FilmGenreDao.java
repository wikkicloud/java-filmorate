package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreDao {
    boolean addGenre(Long filmId, int genreId);

    boolean removeGenre(Long filmId, int genreId);

    List<Genre> getGenreByFilmId(Long filmId);
}
