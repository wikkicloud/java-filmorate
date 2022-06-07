package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {
    JdbcTemplate jdbcTemplate;
    FilmStorage filmStorage;
    UserStorage userStorage;

    public FilmLikeDaoImpl(
            JdbcTemplate jdbcTemplate,
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId).orElseThrow(() -> new FilmNotFoundException(filmId));
        User user = userStorage.getById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String sqlQuery = "INSERT INTO film_like(film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        log.info("film id: {} add like user id: {}", filmId, userId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId).orElseThrow(() -> new FilmNotFoundException(filmId));
        User user = userStorage.getById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String sqlQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        log.info("film id: {} remove like user id: {}", filmId, userId);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, rm.id AS mpa_id, rm.name AS mpa_name, COUNT(l.USER_ID) AS likes " +
                "FROM film AS f " +
                "LEFT JOIN film_like AS l ON f.id = l.film_id " +
                "JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id " +
                "GROUP BY f.ID " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, new MapRowToFilm(), count);
    }
}
