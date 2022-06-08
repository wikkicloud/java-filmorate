package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmLikeService {
    private final FilmLikeDao filmLikeDao;

    public FilmLikeService(FilmLikeDao likeDao) {
        this.filmLikeDao = likeDao;
    }

    public Film addLike(Long filmId, Long userId) {
        return filmLikeDao.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        return filmLikeDao.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmLikeDao.getPopularFilms(count);
    }
}
