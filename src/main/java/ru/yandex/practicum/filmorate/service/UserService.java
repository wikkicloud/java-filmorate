package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;

@Slf4j
@Service
public class UserService extends GenericService<User> {


    public UserService(@Qualifier("userDbStorage") Storage<User> storage) {
        super(storage);
    }

    @Override
    public void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин {} содержит пробелы или пустой", user.getLogin());
            throw new ValidationException("Логин содержит пробелы или пустой");
        }

        //Используем логин если имя пустое
        if (user.getName() == null ||user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка формата email '{}'", user.getEmail());
            throw new ValidationException("Ошибка формата email");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка даты рождения '{}'", user.getBirthday());
            throw new ValidationException("Ошибка в поле дата рождения");
        }
    }
}
