# ShortURL

This is an API for Url shortener service like tiny url. 

Это RESTful API для сокращателя ссылок. 
Сокращатель ссылок - сервис, который позволяет пользователю создавать более короткие адреса,
которые лучше передавать другим пользователям и собирает статистику по совершенным переходам.
Примеры таких сервисов: https://goo.gl/, https://bitly.com/. Рекомендуемый стек – swagger, spring boot.

# Как собрать 
+ С docker и docker compose: 

```sh
$ git clone https://github.com/kain69/ShortUrl.git
$ cd ShortURL 
$ docker-compose up 
```

    - Открыть localhost:8080/swagger-ui 

- Без Docker: 
```sh
$ git clone https://github.com/kain69/ShortUrl.git
```
    - Убедитесь, что у вас есть доступ к серверу PostgreSQL.
    - Откройте проект в редакторе и измените файл application.yml, чтобы он указывал на вашу базу данных PostgreSQL.
    - Соберите проект Spring.
    - Для работоспособности регистрации нужно выполнить SQL находящийся в initdb
    - Откройте localhost:8080/swagger-ui
# Как пройти регистрацию
```
  POST localhost:8080/api/signup
  BODY 
      {
        "username": "string",
        "password": "string",
        "roles": [
          "string" // "user"
        ]
      }
```
