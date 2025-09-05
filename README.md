# User Management System

## Overview

Микросервисная система для управления пользователями с автоматической системой уведомлений. Система состоит из двух основных сервисов: User Service и Notification Service, взаимодействующих через REST API.

## Prerequisites

- Docker Desktop установлен и запущен
- Порты 8080 и 8081 доступны на локальной машине

## Quick Start

1. Клонируйте репозиторий проекта
2. Перейдите в корневую директорию с файлом `docker-compose.yml`
3. Выполните команду:

```
docker-compose up --build
```
## Test Data

После успешного запуска в системе будут предустановлены следующие пользователи:
```markdown
  {
    "id": 1,
    "userName": "a1",
    "password": "a1",
    "email": "razdymakhosaveliy@gmail.com",
    "firstName": "Gena",
    "lastName": "Volkov",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "userName": "a2",
    "password": "a2",
    "email": "saveliyrazdymakho@gmail.com",
    "firstName": "Oleg",
    "lastName": "Semin",
    "role": "ADMIN"
  },
  {
    "id": 3,
    "userName": "u2",
    "password": "u2",
    "email": "asdasqwe@gmail.com",
    "firstName": "Genadwa",
    "lastName": "Volkovsda",
    "role": "USER"
  }
```
## Authentication
### User Registration
Endpoint:

POST ``` http://localhost:8080/api/v1/auth/register```

Request Body:
```markdown
{
  "username": "a3",
  "password": "a3",
  "email": "example@gmail.com",
  "firstName": "Name",
  "lastName": "LastName",
  "role": "ADMIN"
}
```

Response:
```markdown
{
  "id": 5,
  "userName": "a3",
  "password": "$2a$10$QNGwcjoY6J2Ru1MGJxWHau8rMplpKXd9Bg/4Iabinsw0cuaiBgg9a",
  "email": "example@gmail.com",
  "firstName": "Name",
  "lastName": "LastName",
  "role": "ADMIN"
}
```
### User Login

Endpoint:

POST  ``` http://localhost:8080/api/v1/auth/login ```

Request Body:
```markdown
{
  "username": "a3",
  "password": "a3"
}
```

Response:
```markdown
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMyIsImlkIjo1LCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3NTcwOTc0ODYsImV4cCI6MTc1NzE4Mzg4Nn0.BOYDYMiR-6c3PmjLNpVyyjkUr0Nnubik4bQsPHTHlWw"
}
```

### Полученный JWT токен должен использоваться в заголовке Authorization для последующих запросов:
```markdown
Authorization: Bearer <your_token>
```
## User Management API

### Get All Users
GET ``` http://localhost:8080/api/v1/users```

Доступ: только ADMIN

### Get User by ID
GET ``` http://localhost:8080/api/v1/users/{id}```

Доступ: ADMIN или владелец аккаунта

### Update User
PUT ``` http://localhost:8080/api/v1/users/{id}```

Доступ: ADMIN для любых аккаунтов, USER только для своего

Request Body:
```marksdown
{
  "username": "a2",
  "password": "a2",
  "email": "saveliyrazdymakho@gmail.com",
  "firstName": "Oleg",
  "lastName": "Semin"
}
```

### Delete User
Endpoint:

DELETE  ``` http://localhost:8080/api/v1/users/{id}```

Доступ: ADMIN или владелец аккаунта
Response: HTTP 204 No Content

## Notification Service

### Система автоматически отправляет уведомления всем администраторам при:
💠создании нового пользователя с ролью ADMIN

💠изменении данных существующего ADMIN пользователя

💠удалении ADMIN пользователя

Endpoint:

GET  ``` http://localhost:8081/api/notifications```

```Тестовый метод для проверки наличия созданных уведомлений в базе ```

### 🔒 Security отключен для методов Notification Service, так как уведомления отправляются автоматически.

## Architecture Notes
```text
Изначально система использовала Apache Kafka для асинхронной обработки уведомлений.
В текущей реализации для упрощения развертывания используется синхронное взаимодействие через Feign Client между сервисами.
