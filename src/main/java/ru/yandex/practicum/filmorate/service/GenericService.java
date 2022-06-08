package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Service
abstract class GenericService<T extends Entity> {

    protected final Storage<T> storage;

    @Autowired
    protected GenericService(Storage<T> storage) {
        this.storage = storage;
    }

    public T create(T t) {
        validate(t);
        return storage.create(t);
    }

    public T update(T t) {
        validate(t);
        storage.getById(t.getId()).orElseThrow(() -> new NotFoundException(t.getId()));
        return storage.update(t);
    }

    public T getById(Long id) {
        return storage.getById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<T> findAll() {
        return storage.findAll();
    }

    public abstract void validate(T t);
}
