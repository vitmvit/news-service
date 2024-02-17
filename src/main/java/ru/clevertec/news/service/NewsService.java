package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;

public interface NewsService {

    NewsDto findById(Long id);

    Page<NewsDto> findAll(Integer offset, Integer limit);

    Page<NewsDto> searchByText(Integer offset, Integer limit, String fragment);

    Page<NewsDto> searchByTitle(Integer offset, Integer limit, String fragment);

    NewsDto create(NewsCreateDto dto);

    NewsDto update(NewsUpdateDto dto);

    void delete(Long id);
}
