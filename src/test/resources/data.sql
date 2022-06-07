--Заполняем таблицу с рейтингом
INSERT INTO PUBLIC.RATING_MPA (NAME)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');
INSERT INTO USER
    (NAME, LOGIN, BIRTHDAY, EMAIL)
VALUES
    ('tom', 'tomtom', '1980-12-5', 'tom@tom.com'),
    ('friend', 'pirate', '1930-5-7', 'yarr@tortu.ga'),
    ('saul', 'bettercallhim', '1987-4-8', 'saul@good.man');

INSERT INTO FILM
    (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_MPA_ID)
VALUES
    ('Pulp Friction', 'about friction and pulp', '1993-5-22', 180, 4),
    ('Titanic', 'underwater drama', '1991-3-15', 150, 3);

insert into GENRE (NAME)
values  ('Action'),
        ('Western'),
        ('Gangster movie'),
        ('Detective'),
        ('Drama'),
        ('Historical movie'),
        ('Comedy'),
        ('Melodrama'),
        ('Musical movie'),
        ('Noire'),
        ('Political movie'),
        ('Adventure movie'),
        ('Tale'),
        ('Tragedy'),
        ('Tragic comedy'),
        ('Thriller'),
        ('Sci-fi  movie'),
        ('Horror'),
        ('Catastrophe movie'),
        ('Fantasy');