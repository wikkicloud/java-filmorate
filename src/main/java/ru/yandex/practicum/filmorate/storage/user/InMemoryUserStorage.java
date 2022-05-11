package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static Long globalId = 1L;

    private static Long getNextId() {
        return globalId++;
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getByID(Long id) {
        return users.get(id);
    }
}
