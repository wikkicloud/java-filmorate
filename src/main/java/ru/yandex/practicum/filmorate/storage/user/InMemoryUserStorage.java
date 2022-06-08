package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {
  //Перезагружаем ID что-бы работали тесты
    private Long globalId = 1L;
}
