package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер для работы с комментариями
 */
@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * Получение комментария по его идентификатору
     *
     * @param id идентификатор комментария
     * @return объект ResponseEntity с найденным комментарием типа CommentDto и статусом OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findCommentById(id));
    }

    /**
     * Поиск комментариев по текстовому фрагменту
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment текстовый фрагмент для поиска
     * @return объект ResponseEntity со списком найденных комментариев типа Page CommentDto и статусом OK
     */
    @GetMapping("/search/text/{text}")
    ResponseEntity<Page<CommentDto>> searchCommentsByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                          @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                          @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchCommentsByText(offset, limit, fragment));
    }

    /**
     * Поиск комментариев по фрагменту имени пользователя
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment фрагмент имени пользователя для поиска
     * @return объект ResponseEntity со списком найденных комментариев типа Page CommentDto и статусом OK
     */
    @GetMapping("/search/username/{username}")
    ResponseEntity<Page<CommentDto>> searchCommentsByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                              @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                              @PathVariable("username") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchCommentsByUsername(offset, limit, fragment));
    }

    /**
     * Создание нового комментария
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с созданным комментарием типа CommentDto и статусом CREATED
     */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(commentCreateDto, auth));
    }

    /**
     * Обновление комментария
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с обновленным комментарием типа CommentDto и статусом OK
     */
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentUpdateDto commentUpdateDto, @RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(commentUpdateDto, auth));
    }

    /**
     * Удаление комментария по его идентификатору
     *
     * @param id     идентификатор комментария
     * @param userId идентификатор пользователя, связанного с комментарием
     * @param auth   строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity со статусом NO_CONTENT
     */
    @DeleteMapping("/{id}/{user-id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth) {
        commentService.deleteComment(id, userId, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
