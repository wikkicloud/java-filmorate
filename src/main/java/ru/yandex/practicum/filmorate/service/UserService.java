package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService  extends GenericService<User> {

    public UserService(Storage<User> storage) {
        super(storage);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = storage.getByID(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User userAdded = storage.getByID(friendId).orElseThrow(() -> new UserNotFoundException(friendId));

        //Если удалось добавить обновим список друзей
        if (user.getFriends().add(friendId)) {
            log.info("user id: {} add friend user id: {}", userId, friendId);
            //Обновим список друзей и у добавляемого друга
            userAdded.getFriends().add(userId);
            log.info("user id: {} add friend user id: {}", friendId, userId);
        }
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = storage.getByID(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User userRemoved = storage.getByID(friendId).orElseThrow(() -> new UserNotFoundException(friendId));

        if (user.getFriends().remove(friendId)) {
            log.info("user id: {} remove from friends user id: {}", userId, friendId);
        }
        if (userRemoved.getFriends().remove(userId)) {
            log.info("user id: {} remove from friends user id: {}", friendId, userId);
        }
        return user;
    }

    public List<User> getFriends(Long id) {
        User user = storage.getByID(id).orElseThrow(() -> new UserNotFoundException(id));
        return storage.findAll().stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getFriendsCommonOtherUser(Long id, Long otherId) {
        User user = storage.getByID(id).orElseThrow(() -> new UserNotFoundException(id));
        User otherUser = storage.getByID(otherId).orElseThrow(() -> new UserNotFoundException(otherId));

        //Получаем ID общих друзей
        List<Long> commonListFriends = user.getFriends().stream()
                .filter(aLong -> otherUser.getFriends().contains(aLong))
                .collect(Collectors.toList());

        return storage.findAll().stream()
                .filter(u -> commonListFriends.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин {} содержит пробелы или пустой", user.getLogin());
            throw new ValidationException("Логин содержит пробелы или пустой");
        }

        //Используем логин если имя пустое
        if (user.getName().isEmpty()) {
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
