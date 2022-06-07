package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public abstract class InMemoryStorage<T extends Entity> implements Storage<T> {
    private final Map<Long, T> data = new HashMap<>();
    protected Long globalId = 1L;

    public Long getNextId() {
        return globalId++;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public T create(T t) {
        t.setId(getNextId());
        data.put(t.getId(), t);
        log.info("Добавлен {}", t);
        return t;
    }

    @Override
    public T update(T t) {
        data.put(t.getId(), t);
        log.info("Обновлен {}",  t);
        return t;
    }

}
