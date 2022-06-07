package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class FilmGenreService {
    FilmGenreDao filmGenreDao;

    public FilmGenreService(FilmGenreDao filmGenreDao) {
        this.filmGenreDao = filmGenreDao;
    }

    boolean addGenre(Long filmId, int genreId) {
        return filmGenreDao.addGenre(filmId, genreId);
    }

    boolean removeGenre(Long filmId, int genreId) {
        return filmGenreDao.removeGenre(filmId, genreId);
    }

    List<Genre> getGenreByFilmId(Long filmId) {
        return filmGenreDao.getGenreByFilmId(filmId);
    }
}
