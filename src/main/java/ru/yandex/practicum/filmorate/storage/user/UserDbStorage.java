package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.MapRowToUser;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO user(name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName()); //name
            stmt.setString(2, user.getLogin()); //login
            stmt.setString(3, user.getEmail()); //email
            stmt.setString(4, user.getBirthday().toString() //birthday
            );
            return stmt;
        }, keyHolder);

        Long idUser = Objects.requireNonNull(keyHolder.getKey()).longValue();
        //ID сохраненой записи в БД
        user.setId(idUser);

        log.info("Добавлен {}", user);
        return user;
    }

    @Override
    public User update(User userToUpdate) {
        Long userUpdateId = userToUpdate.getId();
        User user = getById(userUpdateId).orElseThrow(() -> new UserNotFoundException(userUpdateId));
        user.setName(userToUpdate.getName());
        user.setLogin(userToUpdate.getLogin());
        user.setEmail(userToUpdate.getEmail());
        user.setBirthday(userToUpdate.getBirthday());
        String sqlQuery = "UPDATE user SET " +
                "name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(), //name
                user.getLogin(), //login
                user.getEmail(), //email
                user.getBirthday(), //birthday
                userUpdateId //id
        );
        log.info("Обновлен {}",  user);
        return user;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM user";
        return jdbcTemplate.query(sqlQuery, new MapRowToUser());
    }

    @Override
    public Optional<User> getById(Long id) {
        try {
            String sqlQuery = "SELECT * FROM user WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sqlQuery, new MapRowToUser(), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }
}
