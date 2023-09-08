# ShortURL

Это RESTful API для сокращателя ссылок. 
Сокращатель ссылок - сервис, который позволяет пользователю создавать более короткие адреса,
которые лучше передавать другим пользователям и собирает статистику по совершенным переходам.
Примеры таких сервисов: https://goo.gl/, https://bitly.com/. Рекомендуемый стек – swagger, spring boot.

# Как собрать 
+ С docker и docker compose: 

```sh
$ git clone https://github.com/kain69/ShortUrl.git
$ cd ShortUrl 
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
  POST localhost:8080/api/auth/signup
  BODY 
      {
        "username": "string",
        "password": "string",
        "roles": [
          "string" // "user"
        ]
      }
```

Авторизация реализована, но не используется при запросах по пользователю.
Вместо этого запросы по поиску используют userId напрямую.
