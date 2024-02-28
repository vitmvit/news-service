package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Feign-клиент для взаимодействия с микросервисом комментариев
 */
@FeignClient(contextId = "commentClient", value = "commentService", url = "http://localhost:8083/api/comments")
public interface CommentClient {

    /**
     * Получение комментария по его идентификатору
     *
     * @param id идентификатор комментария
     * @return объект CommentDto с найденным комментарием
     */
    @GetMapping("/{id}")
    CommentDto getById(@PathVariable("id") Long id);

    /**
     * Получение списка комментариев для определенной новости с пагинацией
     *
     * @param offset смещение (начальный индекс комментариев)
     * @param limit  количество комментариев на странице
     * @param id     идентификатор новости
     * @return объект Page<CommentDto> со списком комментариев для новости
     */
    @GetMapping("news-id/{id}")
    Page<CommentDto> getByNewsId(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                 @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                 @PathVariable("id") Long id);

    /**
     * Получение списка всех комментариев с пагинацией
     *
     * @param offset смещение (начальный индекс комментариев)
     * @param limit  количество комментариев на странице
     * @return объект Page<CommentDto> со списком всех комментариев
     */
    @GetMapping
    Page<CommentDto> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                            @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit);

    /**
     * Поиск комментариев по текстовому фрагменту с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment текстовый фрагмент для поиска
     * @return объект Page<CommentDto> со списком найденных комментариев
     */
    @GetMapping("/search/text/{text}")
    Page<CommentDto> searchByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                  @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                  @PathVariable("text") String fragment);

    /**
     * Поиск комментариев по фрагменту имени пользователя с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment фрагмент имени пользователя для поиска
     * @return объект Page<CommentDto> со списком найденных комментариев
     */
    @GetMapping("/search/username/{username}")
    Page<CommentDto> searchByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                      @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                      @PathVariable("username") String fragment);

    /**
     * Создание нового комментария
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект CommentDto с созданным комментарием
     */
    @PostMapping
    CommentDto create(@RequestBody CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String auth);

    /**
     * Обновление существующего комментария
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект CommentDto с обновленным комментарием
     */
    @PutMapping
    CommentDto update(@RequestBody CommentUpdateDto commentUpdateDto, @RequestHeader("Authorization") String auth);

    /**
     * Удаление комментария по его идентификатору
     *
     * @param id     идентификатор комментария
     * @param userId идентификатор пользователя, связанного с комментарием
     * @param auth   строка заголовка Authorization, содержащая JWT-токен
     */
    @DeleteMapping("/{id}/{user-id}")
    void delete(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth);
}
