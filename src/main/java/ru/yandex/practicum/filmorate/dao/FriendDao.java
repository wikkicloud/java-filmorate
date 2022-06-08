package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    User addFriend(Long userId, Long friendId);

    boolean removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> getFriendsCommonOtherUser(Long id, Long otherId);
}
