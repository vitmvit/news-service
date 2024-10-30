package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.page.PageContentDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Feign-клиент для взаимодействия с микросервисом комментариев
 */
@FeignClient(contextId = "commentClient", value = "${feign.comments-service.value}", url = "${feign.comments-service.url}")
public interface CommentClient {

    /**
     * Получение списка комментариев для определенной новости с пагинацией
     *
     * @param pageNumber смещение (начальный индекс комментариев)
     * @param pageSize   количество комментариев на странице
     * @param id         идентификатор новости
     * @return объект Page CommentDto со списком комментариев для новости
     */

    @GetMapping("newsId/{id}")
    PageContentDto<CommentDto> getByNewsId(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                           @PathVariable("id") Long id);
}