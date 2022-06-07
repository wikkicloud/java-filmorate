package ru.yandex.practicum.filmorate.model;

public class Friend {
    private Long personId;
    private Long friendId;
    private boolean confirmed;

    public Friend(Long personId, Long friendId, boolean confirmed) {
        this.personId = personId;
        this.friendId = friendId;
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
