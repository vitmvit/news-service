package ru.clevertec.news.service;

import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.model.dto.NewsFilterDto;

public interface NewsService {

    NewsDto findNewsById(Long id);

    NewsDto findNewsByIdWithComments(Integer pageNumber, Integer pageSize, Long id);

    PageContentDto<NewsDto> findAll(PageParamDto param, NewsFilterDto filter);

    NewsDto create(NewsCreateDto dto);

    NewsDto update(NewsUpdateDto dto);

    void delete(Long id);
}