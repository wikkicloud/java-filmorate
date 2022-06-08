package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO film(name, release_date, description, duration, rating_mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName()); //name
            stmt.setString(2, film.getReleaseDate().toString()); //release_date
            stmt.setString(3, film.getDescription()); //description
            stmt.setInt(4, film.getDuration()); //duration
            stmt.setInt(5, film.getMpa().getId()); //mpa_id
            return stmt;
        }, keyHolder);

        Long idFilm = Objects.requireNonNull(keyHolder.getKey()).longValue();
        //ID сохраненой записи в БД
        film.setId(idFilm);

        log.info("Добавлен {}", film);
        return film;
    }

    @Override
    public Film update(Film filmToUpdate) {
        String sqlQuery = "UPDATE film SET " +
                "name = ?, release_date = ?, description = ?, duration = ?, rating_mpa_id = ? " +
                "WHERE id = ?";
        Film film = getById(filmToUpdate.getId()).orElseThrow(() -> new FilmNotFoundException(filmToUpdate.getId()));
        film.setName(filmToUpdate.getName());
        film.setReleaseDate(filmToUpdate.getReleaseDate());
        film.setDescription(filmToUpdate.getDescription());
        film.setDuration(filmToUpdate.getDuration());
        film.setMpa(filmToUpdate.getMpa());
        jdbcTemplate.update(sqlQuery,
                            film.getName(), //name
                            film.getReleaseDate(), //release_date
                            film.getDescription(), //description
                            film.getDuration(), //duration
                            film.getMpa().getId(), //mpa_id
                            film.getId()); //id
        log.info("Обновлен {}",  film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT f.*, rm.id AS mpa_id, rm.name AS mpa_name " +
                "FROM film AS f "+
                "JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id";
        return jdbcTemplate.query(sqlQuery, new MapRowToFilm());
    }

    @Override
    public Optional<Film> getById(Long id) {
        try {
            String sqlQuery = "SELECT f.*, rm.id AS mpa_id, rm.name AS mpa_name " +
                    "FROM film AS f "+
                    "JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id "+
                    "WHERE f.id = ?";
            Film film = jdbcTemplate.queryForObject(sqlQuery, new MapRowToFilm(), id);
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(id);
        }
    }
}
