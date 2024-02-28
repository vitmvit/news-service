package ru.clevertec.news.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.CommentService;

/**
 * Реализация сервиса комментариев
 */
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentClient commentClient;

    /**
     * Получение комментария по его идентификатору
     *
     * @param id идентификатор комментария
     * @return объект CommentDto с найденным комментарием
     * @throws EntityNotFoundException если комментарий не найден
     */
    @Override
    public CommentDto findCommentById(Long id) {
        try {
            return commentClient.getById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Поиск комментариев по текстовому фрагменту с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment текстовый фрагмент для поиска
     * @return объект Page<CommentDto> со списком найденных комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchCommentsByText(Integer offset, Integer limit, String fragment) {
        try {
            return commentClient.searchByText(offset, limit, fragment);
        } catch (Exception e) {
            throw new EmptyListException();
        }
    }

    /**
     * Поиск комментариев по фрагменту имени пользователя с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment фрагмент имени пользователя для поиска
     * @return объект Page<CommentDto> со списком найденных комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchCommentsByUsername(Integer offset, Integer limit, String fragment) {
        try {
            return commentClient.searchByUsername(offset, limit, fragment);
        } catch (Exception e) {
            throw new EmptyListException();
        }
    }

    /**
     * Создание комментария
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект CommentDto с созданным комментарием
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @Override
    public CommentDto createComment(CommentCreateDto commentCreateDto, String auth) {
        try {
            return commentClient.create(commentCreateDto, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }

    /**
     * Обновление комментария
     *
     * @param commentUpdateDto данные для обновления комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект CommentDto с обновленным комментарием
     * @throws EntityNotFoundException если комментарий не найден
     * @throws NoAccessError           если отсутствует доступ или неверный JWT-токен
     */
    @Override
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, String auth) {
        try {
            commentClient.getById(commentUpdateDto.getId());
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
        try {
            return commentClient.update(commentUpdateDto, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }

    /**
     * Удаление комментария по его идентификатору
     *
     * @param id     идентификатор комментария
     * @param userId идентификатор пользователя, связанного с комментарием
     * @param auth   строка заголовка Authorization, содержащая JWT-токен
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @Override
    public void deleteComment(Long id, Long userId, String auth) {
        try {
            commentClient.delete(id, userId, auth);
        } catch (Exception e) {
            throw new NoAccessError();
        }
    }
}
