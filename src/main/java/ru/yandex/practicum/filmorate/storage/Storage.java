package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    T create(T t);

    T update(T t);

    List<T> findAll();

    Optional<T> getById(Long id);
}
