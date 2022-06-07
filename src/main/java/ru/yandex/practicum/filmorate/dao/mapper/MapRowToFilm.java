package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MapRowToFilm implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));

        return new Film(id, name, description, releaseDate, duration, mpa);
    }
}
