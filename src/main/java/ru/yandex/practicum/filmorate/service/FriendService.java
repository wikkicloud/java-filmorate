package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class FriendService {
    private final FriendDao friendDao;

    public FriendService(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public User addFriend(Long userId, Long friendId) {
        return friendDao.addFriend(userId, friendId);
    }

    public boolean removeFriend(Long userId, Long friendId) {
        return friendDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        return friendDao.getFriends(id);
    }

    public List<User> getFriendsCommonOtherUser(Long id, Long otherId) {
        return friendDao.getFriendsCommonOtherUser(id, otherId);
    }
}
