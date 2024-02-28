# news-service

Данный микросервис работает с новостями

## Swagger

http://localhost:8082/api/doc/swagger-ui/index.html#/

## Реализация:

В каждом запросе необходимо передавать token в заголовке

### NewsController

Контроллер поддерживает следующие операции:

- получение новости по id (без комментария)
- получение всех комментариев по id новости
- получение всех новостей
- поиск по фрагменту текста
- поиск по фрагменту заголовка новости
- создание новости
- редактирование новости
- удаление новости

#### GET запрос getAll()

Request:

```http request
http://localhost:8082/api/news?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
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

#### GET запрос searchByTitle(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8082/api/news/search/title/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
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

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8082/api/news/search/text/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T16:31:38.166",
      "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
      "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
      "userId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T16:31:39.772",
      "title": "Global Economic Growth Expected to Slow Down in the Next Quarter",
      "text": "Economists predict a decline in the pace of global economic expansion in the upcoming quarter due to various factors, including trade tensions and geopolitical uncertainties.",
      "userId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T16:31:40.477",
      "title": "New Study Reveals Link Between Sleep and Productivity",
      "text": "A recent scientific study suggests a strong correlation between quality sleep patterns and enhanced productivity levels, highlighting the importance of adequate rest for optimal performance.",
      "userId": 3
    },
    {
      "id": 4,
      "time": "2024-02-17T16:31:41.232",
      "title": "World Leaders Gather for Climate Change Summit in Paris",
      "text": "Heads of state and global leaders convene in Paris to discuss urgent measures and collaborate on combating climate change and implementing sustainable practices worldwide.",
      "userId": 4
    },
    {
      "id": 5,
      "time": "2024-02-17T16:31:41.232",
      "title": "Breakthrough in Cancer Research Could Lead to New Treatment Options",
      "text": "Scientists have achieved a significant breakthrough in cancer research, potentially opening doors to innovative treatment methods that could revolutionize cancer care.",
      "userId": 5
    },
    {
      "id": 6,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Giant Unveils Next Generation Smartphone with Revolutionary Features",
      "text": "A prominent technology company introduces a cutting-edge smartphone model equipped with groundbreaking features designed to enhance user experience and redefine the industry standard.",
      "userId": 7
    },
    {
      "id": 7,
      "time": "2024-02-17T16:31:41.232",
      "title": "Sports Team Makes Historic Comeback in Championship Game",
      "text": "Against all odds, a determined sports team stages an extraordinary comeback to win a crucial championship game, creating history in the world of sports.",
      "userId": 6
    },
    {
      "id": 8,
      "time": "2024-02-17T16:31:41.232",
      "title": "Renowned Artists Exhibition Receives Rave Reviews from Critics",
      "text": "A celebrated artists latest exhibition garners overwhelming praise and accolades from art critics and enthusiasts alike, solidifying their position as a leading figure in the art world.",
      "userId": 6
    },
    {
      "id": 9,
      "time": "2024-02-17T16:31:41.232",
      "title": "Scientists Develop Promising Vaccine for Deadly Disease",
      "text": "Researchers announce a promising breakthrough in vaccine development, potentially offering a new solution for combating a life-threatening disease that has plagued communities for years.",
      "userId": 8
    },
    {
      "id": 10,
      "time": "2024-02-17T16:31:41.232",
      "title": "Major Cybersecurity Breach Exposes Millions of User Data",
      "text": "A significant cybersecurity breach leads to the exposure of sensitive user information, raising concerns about data privacy and the need for enhanced online security measures.",
      "userId": 9
    },
    {
      "id": 11,
      "time": "2024-02-17T16:31:41.232",
      "title": "Political Unrest Escalates in Troubled Region",
      "text": "The tense situation in a troubled region reaches a boiling point as political unrest escalates.",
      "userId": 9
    },
    {
      "id": 12,
      "time": "2024-02-17T16:31:41.232",
      "title": "Record-breaking Heatwave Sweeps Across Several Countries",
      "text": "Unprecedented heatwave conditions scorch several countries, setting new temperature records.",
      "userId": 8
    },
    {
      "id": 13,
      "time": "2024-02-17T16:31:41.232",
      "title": "New Study Shows Link Between Social Media Use and Mental Health Issues",
      "text": "A recent study highlights the correlation between excessive social media use and mental health issues.",
      "userId": 7
    },
    {
      "id": 14,
      "time": "2024-02-17T16:31:41.232",
      "title": "Tech Company Announces Plans to Launch Lunar Tourism Program",
      "text": "Exciting plans are underway as a tech company reveals intentions to launch a program for lunar tourism.",
      "userId": 6
    },
    {
      "id": 15,
      "time": "2024-02-17T16:31:41.232",
      "title": "Wildfire Threatens Residential Areas, Evacuations Ordered",
      "text": "Residential areas face imminent danger as a raging wildfire threatens homes, prompting necessary evacuations.",
      "userId": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": [],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 15,
  "number": 0,
  "sort": [],
  "numberOfElements": 15,
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

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
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

Если текущий пользователь имеет доступ к этой функции, но обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос deleteNews(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8082/api/news/1/4
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

### CommentController (Feign)

Контроллер поддерживает следующие операции:

- получение комментария по id
- поиск по фрагменту текста
- поиск по фрагменту имени пользователя
- создание комментария
- редактирование комментария
- удаление комментария

#### GET запрос getCommentById(Long id)

Request:

```http request
http://localhost:8082/api/comments/7
```

Response:

```json
{
  "id": 7,
  "time": "2024-02-17T19:06:01.405",
  "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
  "username": "techguru99",
  "newsId": 4
}
```

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getByIdWithComments(Long id)

Request:

```http request
http://localhost:8082/api/news/1/comments?offset=0&limit=15
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T16:31:38.166",
  "title": "Scientists Discover New Species of Marine Life in the Atlantic Ocean",
  "text": "Researchers have recently identified a previously unknown species of marine organisms during a deep-sea expedition in the Atlantic Ocean.",
  "comments": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    }
  ],
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

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8082/api/comments/search/text/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 10,
      "time": "2024-02-17T19:06:01.405",
      "text": "Thank you for sharing this information. Its eye-opening and thought-provoking. Keep up the great work!",
      "username": "smarthacker76",
      "newsId": 10
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "username123",
      "newsId": 1
    }
  ],
  "number": 0,
  "size": 15,
  "totalElements": 9,
  "totalPages": 1,
  "numberOfElements": 9,
  "hasContent": true,
  "first": true,
  "last": true
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByUsername(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8082/api/comments/search/username/u?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "username123",
      "newsId": 1
    }
  ],
  "number": 0,
  "size": 15,
  "totalElements": 8,
  "totalPages": 1,
  "numberOfElements": 8,
  "hasContent": true,
  "first": true,
  "last": true
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST запрос createComment(CommentCreateDto commentCreateDto, String auth)

Request:

```http request
http://localhost:8082/api/comments
```

Body:

```json
{
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Response:

```json
{
  "id": 21,
  "time": "2024-02-20T21:45:23.910",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT запрос updateComment(CommentUpdateDto commentsUpdateDto, String auth)

Request:

```http request
http://localhost:8082/api/comments
```

Body:

```json
{
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1,
  "id": 2
}
```

Response:

```json
{
  "id": 2,
  "time": "2024-02-17T19:06:01.405",
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь имеет доступ к этой функции, но обновляемая новость не найдена:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос deleteComment(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8082/api/comments/1/3
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```


