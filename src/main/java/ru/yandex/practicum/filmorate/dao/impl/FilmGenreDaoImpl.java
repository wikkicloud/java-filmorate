package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.mapper.MapRowToGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {

    JdbcTemplate jdbcTemplate;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addGenre(Long filmId, int genreId) {
        String sqlQuery = "INSERT INTO film_genre(film_id, genre_id) " +
                "VALUES (?, ?)";
        return jdbcTemplate.update(sqlQuery, filmId, genreId) > 0;
    }

    @Override
    public boolean removeGenre(Long filmId, int genreId) {
        String sqlQuery = "DELETE FROM film_genre " +
                "WHERE film_id = ? AND genre_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, genreId) > 0;
    }

    @Override
    public List<Genre> getGenreByFilmId(Long filmId) {
        String sqlQuery = "SELECT g.* FROM film_genre AS fg " +
                "JOIN genre AS g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sqlQuery, new MapRowToGenre(), filmId);
    }
}
