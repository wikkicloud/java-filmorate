package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() == null || filmStorage.getByID(film.getId()) == null) {
            throw new FilmNotFoundException(film.getId());
        }
        validate(film);
        return filmStorage.update(film);
    }

    public Film getById(Long id) {
        Film film = filmStorage.getByID(id);
        if (film == null)
            throw new FilmNotFoundException(id);
        return film;
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getByID(filmId);
        User user = userStorage.getByID(userId);
        if (film == null)
            throw new FilmNotFoundException(filmId);
        if (user == null)
            throw new UserNotFoundException(userId);

        //Если добавился like обновим список like у фильма
        if (film.getUsersLiked().add(userId)) {
            log.info("film id: {} add like user id: {}", filmId, userId);
        }
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getByID(filmId);
        User user = userStorage.getByID(userId);
        if (film == null)
            throw new FilmNotFoundException(filmId);
        if (user == null)
            throw new UserNotFoundException(userId);

        //Если лайк убрали, обновим список лайков
        if (film.getUsersLiked().remove(userId)) {
            log.info("film id: {} remove like user id: {}", filmId, userId);
        }
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> (o1.getUsersLiked().size() - o2.getUsersLiked().size()) * -1)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое имя фильма");
            throw new ValidationException("Пустое имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Пустое описание фильма");
            throw new ValidationException("Пустое описание фильма");
        }

        if (film.getDescription().length() > Constants.MAX_SIZE_DESCRIPTION) {
            log.error("Описание фильма не может быть больше {}", Constants.MAX_SIZE_DESCRIPTION);
            throw new ValidationException("Описание фильма не может быть больше" + Constants.MAX_SIZE_DESCRIPTION);
        }

        if (film.getReleaseDate().isBefore(Constants.MIN_DATE_RELEASE)) {
            log.error("Дата релиза фильма {} это раньше {}", film.getReleaseDate(), Constants.MIN_DATE_RELEASE);
            throw new ValidationException("Дата релиза фильма раньше " + Constants.MIN_DATE_RELEASE);
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }
    }
}
