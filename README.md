# news-service

Данный микросервис работает с новостями.

Является частью [этого проекта](https://github.com/vitmvit/core-service/tree/dev)

## Swagger

http://localhost:8082/api/doc/swagger-ui/index.html#/

## Реализация:

### NewsController

Контроллер поддерживает следующие операции:

- получение новости по id (без комментария)
- получение всех комментариев по id новости
- получение всех новостей (фильтр по фрагменту и заголовку)
- создание новости
- редактирование новости
- удаление новости

#### GET запрос PageContentDto<NewsDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "text", required = false) String text)

Request:

```http request
http://localhost:8082/api/news?offset=0&limit=15&title=Escalates in Troubled
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 2
  },
  "content": [
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 1
    }
  ]
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос getById(Long id)

Request:

```http request
http://localhost:8082/api/news/1
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
  "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
  "userId": 1
}
```

Если новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### POST запрос createNews(NewsCreateDto newsCreateDto, String auth)

Request:

```http request
http://localhost:8082/api/news
```

Body:

```json
{
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "title": "Political Unrest Escalates in Troubled Region",
  "userId": 1
}
```

Response:

```json
{
  "id": 21,
  "time": "2024-02-20T21:48:42.273",
  "title": "Political Unrest Escalates in Troubled Region",
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "userId": 1
}
```

#### PUT запрос updateNews(NewsUpdateDto newsUpdateDto, String auth)

Request:

```http request
http://localhost:8082/api/news
```

Body:

```json
{
  "id": 1,
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "title": "Political Unrest Escalates in Troubled Region",
  "userId": 1
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Political Unrest Escalates in Troubled Region",
  "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
  "userId": 1
}
```

Если обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### DELETE запрос deleteNews(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8082/api/news/1/4
```