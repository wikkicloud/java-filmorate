package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FriendDaoImpl implements FriendDao {
    JdbcTemplate jdbcTemplate;
    UserStorage userStorage;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        //Получаем Пользовталей, что бы убедится, что они есть
        User user = userStorage.getById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User otherUser = userStorage.getById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        String sqlQuery = "INSERT INTO friend(user_id, friend_id, confirmed) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), otherUser.getId(), true);
        log.info("user id: {} add friend user id: {}", userId, friendId);
        return user;
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<User> getFriends(Long id) {
        String sqlQuery = "SELECT * FROM friend WHERE user_id = ? AND confirmed = TRUE";
        List<Friend> friends = jdbcTemplate.query(sqlQuery, this::makeFriend, id);

        return getUserListFromFriendList(friends);
    }

    @Override
    public List<User> getFriendsCommonOtherUser(Long id, Long otherId) {

        String sqlQuery = "SELECT * FROM friend " +
                "WHERE user_id = ? AND confirmed = TRUE " +
                "AND friend_id IN (" +
                "SELECT friend_id FROM friend " +
                "WHERE user_id = ?)";

        List<Friend> friends = jdbcTemplate.query(sqlQuery, this::makeFriend, id, otherId);

        return getUserListFromFriendList(friends);
    }

    private Friend makeFriend(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(rs.getLong("user_id"),
                rs.getLong("friend_id"),
                rs.getBoolean("confirmed"));
    }

    private List<User> getUserListFromFriendList(List<Friend> friends) {
        return friends.stream()
                .map(Friend::getFriendId)
                .map(userStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
