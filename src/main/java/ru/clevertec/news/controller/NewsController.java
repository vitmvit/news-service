package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.NewsService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер для работы с новостями
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final AuthClient authClient;

    /**
     * Получение списка всех новостей с пагинацией
     *
     * @param offset смещение (начальный индекс новостей)
     * @param limit  количество новостей на странице
     * @return объект ResponseEntity со списком новостей типа Page<NewsDto> и статусом OK
     */
    @GetMapping
    public ResponseEntity<Page<NewsDto>> getAllNews(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                    @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findAllNews(offset, limit));
    }

    /**
     * Получение новости по ее идентификатору
     *
     * @param id идентификатор новости
     * @return объект ResponseEntity с найденной новостью типа NewsDto и статусом OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findNewsById(id));
    }

    /**
     * Получение новости по ее идентификатору с комментариями и пагинацией
     *
     * @param offset смещение (начальный индекс комментариев)
     * @param limit  количество комментариев на странице
     * @param id     идентификатор новости
     * @return объект ResponseEntity с новостью и комментариями типа NewsDto и статусом OK
     */
    @GetMapping("{id}/comments")
    public ResponseEntity<NewsDto> getByIdWithComments(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                       @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                       @PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findNewsByIdWithComments(offset, limit, id));
    }

    /**
     * Поиск новостей по текстовому фрагменту с пагинацией
     *
     * @param offset   смещение (начальный индекс новостей)
     * @param limit    количество новостей на странице
     * @param fragment текстовый фрагмент для поиска
     * @return объект ResponseEntity со списком найденных новостей типа Page<NewsDto> и статусом OK
     */
    @GetMapping("/search/text/{text}")
    public ResponseEntity<Page<NewsDto>> searchNewsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                          @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                          @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchNewsByText(offset, limit, fragment));
    }

    /**
     * Поиск новостей по фрагменту заголовка с пагинацией
     *
     * @param offset   смещение (начальный индекс новостей)
     * @param limit    количество новостей на странице
     * @param fragment текстовый фрагмент для поиска в заголовке новости
     * @return объект ResponseEntity со списком найденных новостей типа Page<NewsDto> и статусом OK
     */
    @GetMapping("/search/title/{title}")
    public ResponseEntity<Page<NewsDto>> searchNewsByTitle(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                           @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                           @PathVariable("title") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchNewsByTitle(offset, limit, fragment));
    }

    /**
     * Создание новости
     *
     * @param newsCreateDto данные для создания новости
     * @param auth          строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с созданной новостью типа NewsDto и статусом CREATED
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @PostMapping
    public ResponseEntity<NewsDto> createNews(@RequestBody NewsCreateDto newsCreateDto, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, newsCreateDto.getUserId(), null)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newsService.createNews(newsCreateDto));
        }
        throw new NoAccessError();
    }

    /**
     * Обновление новости
     *
     * @param newsUpdateDto объект NewsUpdateDto с обновленными данными новости
     * @param auth          строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с обновленной новостью типа NewsDto и статусом OK
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @PutMapping
    public ResponseEntity<NewsDto> updateNews(@RequestBody NewsUpdateDto newsUpdateDto, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, newsUpdateDto.getUserId(), null)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(newsService.updateNews(newsUpdateDto));
        }
        throw new NoAccessError();
    }

    /**
     * Удаление новости по ее идентификатору
     *
     * @param id     идентификатор новости
     * @param userId идентификатор пользователя, связанного с новостью
     * @param auth   строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity со статусом NO_CONTENT
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @DeleteMapping("/{id}/{user-id}")
    public ResponseEntity<Void> deleteNews(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, userId, null)) {
            newsService.deleteNews(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new NoAccessError();
    }
}
