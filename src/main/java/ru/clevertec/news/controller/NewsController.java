package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.model.dto.NewsFilterDto;
import ru.clevertec.news.service.NewsService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер для работы с новостями
 */
@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto getNewsById(@PathVariable("id") Long id) {
        return newsService.findNewsById(id);
    }

    @GetMapping("{id}/comments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public NewsDto getByIdWithComments(@RequestParam(value = "pageNumber", defaultValue = OFFSET_DEFAULT) Integer pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = LIMIT_DEFAULT) Integer pageSize,
                                       @PathVariable("id") Long id) {
        return newsService.findNewsByIdWithComments(pageNumber, pageSize, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageContentDto<NewsDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = OFFSET_DEFAULT) int pageNumber,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "text", required = false) String text) {
        return newsService.findAll(new PageParamDto(pageNumber, pageSize), new NewsFilterDto(title, text));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsDto create(@RequestBody NewsCreateDto newsCreateDto) {
        return newsService.create(newsCreateDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDto update(@RequestBody NewsUpdateDto newsUpdateDto) {
        return newsService.update(newsUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        newsService.delete(id);
    }
}