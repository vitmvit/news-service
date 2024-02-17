package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.feign.CommonClient;
import ru.clevertec.news.service.NewsService;

import java.util.List;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final CommonClient commonClient;
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<NewsDto>> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findAll(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.findById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<NewsDto> getByIdWithComments(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                       @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                       @PathVariable("id") Long id) {
        var newsDto = newsService.findById(id);
        // TODO: 17.02.2024
        newsDto.setComments(commonClient.getByNewsId(offset, limit, id).stream().toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsDto);
    }

    @GetMapping("/{news-id}/comments/{comments-id}")
    public ResponseEntity<NewsDto> getByIdAndCommentId(@PathVariable("news-id") Long newsId,
                                                       @PathVariable("comments-id") Long commentsId) {
        var newsDto = newsService.findById(newsId);
        // TODO: 17.02.2024
        newsDto.setComments(List.of(commonClient.getById(commentsId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsDto);
    }

    @GetMapping("/search/text/{text}")
    public ResponseEntity<Page<NewsDto>> searchByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                      @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                      @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchByText(offset, limit, fragment));
    }

    @GetMapping("/search/title/{title}")
    public ResponseEntity<Page<NewsDto>> searchByTitle(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                       @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                       @PathVariable("title") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.searchByTitle(offset, limit, fragment));
    }

    @PostMapping
    public ResponseEntity<NewsDto> create(@RequestBody NewsCreateDto newsCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newsService.create(newsCreateDto));
    }

    @PutMapping
    public ResponseEntity<NewsDto> update(@RequestBody NewsUpdateDto newsUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newsService.update(newsUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        newsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
