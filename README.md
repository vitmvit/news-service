# news-service

Данный микросервис работает с новостями

## Swagger

http://localhost:8083/api/doc/swagger-ui/index.html#/

## Реализация:

### NewsController

Контроллер поддерживает следующие операции:

- получение новости по id
- получение новости по id (с комментариями по этой новости)
- получение всех новостей
- поиск по фрагменту текста
- поиск по фрагменту заголовка новости
- создание новости
- редактирование новости
- удаление новости

#### GET запрос getById(Long id)

Request:

```http request
http://localhost:8083/api/news/1
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "title",
  "text": "text",
  "comments": null
}
```

Если новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getByIdWithComments(Long id)

Request:

```http request
http://localhost:8083/api/news/1/comments
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "title",
  "text": "text",
  "comments": [
    {
      "id": 1,
      "time": "2024-02-17T18:48:47.626",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ]
}
```

Request:

```http request
http://localhost:8083/api/news/1/comments?offset=0&limit=2
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "title",
  "text": "text",
  "comments": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "text",
      "username": "name",
      "newsId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ]
}
```

Если новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getByIdAndCommentId(Long newsId, Long commentsId)

Request:

```http request
http://localhost:8083/api/news/1/comments/2
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "title",
  "text": "text",
  "comments": [
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ]
}
```

Если новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getAll()

Request:

```http request
http://localhost:8083/api/news?offset=0&limit=10
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "title",
      "text": "text",
      "comments": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 4,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": [],
  "numberOfElements": 4,
  "first": true,
  "empty": false
}
```

Request:

```http request
http://localhost:8083/api/news?offset=1&limit=1
```

Response:

```json
{
  "content": [
    {
      "id": 2,
      "time": "2024-02-17T12:50:03.024",
      "title": "title",
      "text": "text",
      "comments": null
    }
  ],
  "pageable": {
    "pageNumber": 1,
    "pageSize": 1,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 1,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 2,
  "last": true,
  "size": 1,
  "number": 1,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 1,
  "first": false,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8083/api/news/search/text/t
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:29:30.397",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 2,
      "time": "2024-02-17T16:29:31.237",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 3,
      "time": "2024-02-17T16:29:31.582",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 4,
      "time": "2024-02-17T16:29:32.267",
      "title": "title",
      "text": "text",
      "comments": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 4,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 4,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByTitle(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8083/api/comments/search/title/t
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "title",
      "text": "text",
      "comments": null
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "title",
      "text": "text",
      "comments": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 4,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 4,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST запрос create(NewsCreateDto newsCreateDto)

Request:

```http request
http://localhost:8083/api/news
```

Body:

```json
{
  "text": "text",
  "title": "title"
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:12:23.829",
  "title": "title",
  "text": "text",
  "comments": null
}
```

#### PUT запрос update(NewsUpdateDto newsUpdateDto)

Request:

```http request
http://localhost:8083/api/news
```

Body:

```json
{
  "id": 1,
  "title": "title2",
  "text": "text"
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:12:23.829",
  "title": "title2",
  "text": "text",
  "comments": null
}
```

#### DELETE запрос delete(Long id)

Request:

```http request
http://localhost:8083/api/news/2
```

