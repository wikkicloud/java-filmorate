package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    //Перезагружаем ID что-бы работали тесты
    private Long globalId = 1L;
}
