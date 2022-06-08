package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmService filmService;
    @Test
    void shouldValidationExceptionIsNameBlank() {
        Film film = new Film("", "Описание",
                LocalDate.of(2001, Month.JANUARY, 2),90, new Mpa(1, "G"));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }
    @Test
    void shouldValidationExceptionIsLongDesc() {
        String desc201 = "Главной заслугой ленты я считаю драматургию. Я считаю, и думаю многие согласятся," +
                " что вызвать сильную скорбь и грусть гораздо сложнее чем улыбку. Для этого нужно проделать большую" +
                " работу над персонажам";
        Film film = new Film("Фильм", desc201,
                LocalDate.of(2001, Month.JANUARY, 2), 90, new Mpa(1, "G"));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void shouldValidationExceptionIsReleaseDateBeforeMinDateRelease() {
        Film film = new Film("Фильм", "Описание",
                LocalDate.of(1895, Month.DECEMBER, 27), 90, new Mpa(1, "G"));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void shouldValidationExceptionDescriptionIsBlank() {
        Film film = new Film("Фильм", "",
                LocalDate.of(1895, Month.DECEMBER, 27), 90, new Mpa(1, "G"));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void shouldValidationExceptionIsDuration0() {
        Film film = new Film("Фильм", "Описание",
                LocalDate.of(2001, Month.DECEMBER, 27), 0, new Mpa(1, "G"));
        assertThrows(ValidationException.class, () -> filmService.validate(film));
    }

    @Test
    void contextLoads() {
    }
}