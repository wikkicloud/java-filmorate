package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserService userService;
	private final FilmService filmService;
	private final FriendService friendService;
	private final FilmLikeService likeService;


	@Test
	public void testFindUserById() {
		User user = userService.getById(1L);
		Assertions.assertEquals(1, user.getId());
	}

	@Test
	public void testUpdateUser() {
		User newUserForUpdate = new User("light@bulb.clng","moth", null,
				LocalDate.of(1950, 10, 30));
		newUserForUpdate.setId(1L);
		userService.update(newUserForUpdate);

		User updatedUser = userService.getById(1L);
		Assertions.assertEquals(updatedUser.getId(), newUserForUpdate.getId());
		Assertions.assertEquals(newUserForUpdate.getLogin(), updatedUser.getLogin());
		//проверка замены пустого имени логином при обновлении
		Assertions.assertEquals(newUserForUpdate.getLogin(), updatedUser.getName());
		Assertions.assertEquals(newUserForUpdate.getBirthday(), updatedUser.getBirthday());
		Assertions.assertEquals(newUserForUpdate.getEmail(), updatedUser.getEmail());
	}

	@Test
	public void testAddToFriends() {
		User user = userService.getById(1L);
		User friend = userService.getById(2L);
		friendService.addFriend(user.getId(), friend.getId());
		List<User> friends = friendService.getFriends(user.getId());
		Assertions.assertEquals(friends.get(0).getName(), friend.getName());
	}

	@Test
	public void testDeleteFromFriends() {
		User user = userService.getById(1L);
		User friend = userService.getById(2L);
		friendService.removeFriend(user.getId(), friend.getId());
		List<User> friends = friendService.getFriends(user.getId());
		Assertions.assertTrue(friends.isEmpty());
	}

	@Test
	public void testGetAllUsers() {
		Assertions.assertNotNull(userService.findAll());
	}

	@Test
	public void testFindFilmById() {
		Film film = filmService.getById(1L);
		Assertions.assertEquals(1L, film.getId());
	}

	@Test
	public void testUpdateFilm() {
		Film newFilmForUpdate = new Film("Pulp Fiction", "Classic Tarantino movie",
				LocalDate.of(1995, 9, 25), 154, new Mpa(1, "G"));
		newFilmForUpdate.setId(1L);
		filmService.update(newFilmForUpdate);

		Film updatedFilm = filmService.getById(1L);
		Assertions.assertEquals(updatedFilm.getId(), newFilmForUpdate.getId());
		Assertions.assertEquals(updatedFilm.getName(), newFilmForUpdate.getName());
		Assertions.assertEquals(updatedFilm.getDescription(), newFilmForUpdate.getDescription());
		Assertions.assertEquals(updatedFilm.getDuration(), newFilmForUpdate.getDuration());
		Assertions.assertEquals(updatedFilm.getReleaseDate(), newFilmForUpdate.getReleaseDate());
		Assertions.assertEquals(updatedFilm.getMpa(), newFilmForUpdate.getMpa());
	}

	@Test
	public void testGetAllFilms() {
		Assertions.assertNotNull(filmService.findAll());
	}

}
