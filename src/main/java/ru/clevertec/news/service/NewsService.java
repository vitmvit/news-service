package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

public interface NewsService {

 NewsDto findNewsById(Long id);

    NewsDto findNewsByIdWithComments(Integer offset, Integer limit, Long id);

    Page<NewsDto> findAllNews(Integer offset, Integer limit);

    Page<NewsDto> searchNewsByText(Integer offset, Integer limit, String fragment);

    Page<NewsDto> searchNewsByTitle(Integer offset, Integer limit, String fragment);

    NewsDto createNews(NewsCreateDto dto);

    NewsDto updateNews(NewsUpdateDto dto);

    void deleteNews(Long id);
}
