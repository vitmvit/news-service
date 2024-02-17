package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.news.dto.CommentDto;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@FeignClient(contextId = "commonClient", value = "commentService", url = "http://localhost:8082/api/comments")
public interface CommonClient {

    @GetMapping("/{id}")
    CommentDto getById(@PathVariable("id") Long id);

    @GetMapping("news-id/{id}")
    Page<CommentDto> getByNewsId(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                 @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                 @PathVariable("id") Long id);
}
