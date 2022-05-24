# java-filmorate
Template repository for Filmorate project.  

ER-diagram  
![alt text](https://prnt.sc/BARkj9cZSkf7)
##Примеры запросов к БД
Получить фильм по ID
```
SELECT *
FROM film AS f 
WHERE f.id = ID
```
Получить пользователя по ID
```
SELECT *
FROM user AS u 
WHERE u.id = ID
```
Получение фильмов пользователя по ID пользователя
```
SELECT f.name
FROM like AS l
JOIN film AS f ON f.id = l.film_id
WHERE l.person_id = ID;
```
Топ N наиболее популярных фильмов
```
SELECT f.name
       COUNT(l.like_id) AS likes
FROM film AS f
LEFT JOIN like AS l ON f.id = l.film_id
GROUP BY f.name
ORDER BY likes DESC
LIMIT N;
```
Получить имена друзей пользователя c ID 1
```
SELECT u.name
FROM friend AS f
JOIN user AS u ON f.friend_id = u.id
WHERE f.person_id = 1 AND f.confirmed = TRUE;
```

Список общих друзей ID 1 и ID 2:
```
SELECT f.friend_id
FROM friend AS f
WHERE f.person_id = 1 AND f.confirmed = TRUE AND f.friend_id IN (SELECT f.friend_id
                                        FROM friend AS f
                                        WHERE f.person_id = 2 AND f.confirmed = TRUE);
```
Список друзей со статусом
```
SELECT u.friend_id,
       u.confirmed
FROM friend AS u
WHERE u.person_id = 1
```