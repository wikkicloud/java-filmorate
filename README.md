# java-filmorate
Template repository for Filmorate project.  

ER-diagram
https://prnt.sc/D2ekKsz4_d2c
##Примеры запросов к БД  
Получить фильм по ID
```
SELECT *
FROM film AS f 
WHERE f.film_id = ID
```

Получить пользователя по ID
```
SELECT *
FROM user AS u 
WHERE u.film_id = ID
```

Получение фильмов пользователя по ID пользователя
```
SELECT f.name
FROM like AS l
JOIN film AS f ON f.film_id = l.film_id
WHERE l.person_id=ID;
```
Топ N наиболее популярных фильмов
```
SELECT f.name
       COUNT(l.like_id) AS likes
FROM film AS f
LEFT JOIN like AS l ON f.film_id = l.film_id
GROUP BY f.name
ORDER BY likes DESC
LIMIT N;
```
Получить имена друзей пользователя c ID 1
```
SELECT u.name
FROM friend AS f
JOIN user AS u ON f.friend_id = u.user_id
WHERE f.person_id = 1;
```

Список общих друзей ID 1 и ID 2:
```
SELECT f.friend_id
FROM friend AS f
WHERE f.person_id = 1 AND f.friend_id IN (SELECT f.friend_id
                                        FROM friend AS f
                                        WHERE f.person_id = 2);
```
Список друзей со статусом
```
SELECT u.friend_id,
       CASE f.friend_id = u.person_id THEN 'подтверждённая'
       ELSE 'неподтверждённая'
       END AS status
FROM friend AS u
LEFT JOIN friend AS f ON u.friend_id = f.person_id
WHERE u.person_id = 1
```