package ru.clevertec.news.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.news.converter.NewsConverter;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.dto.util.PageUtils;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.model.dto.NewsFilterDto;
import ru.clevertec.news.model.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.specification.NewsSpecification;

import java.util.List;

/**
 * Реализация сервисного слоя для работы с новостями.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsConverter newsConverter;
    private final CommentClient commentClient;

    /**
     * Возвращает информацию о новости по заданному id.
     *
     * @param id новости
     * @return информация о новости
     * @throws EntityNotFoundException если новость не найдена
     */
    @Cacheable(value = "news", key = "#id")
    @Override
    public NewsDto findNewsById(Long id) {
        log.info("NewsService: find news by id: " + id);
        var news = newsConverter.convert(newsRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        news.setComments(null);
        return news;
    }

    /**
     * Возвращает информацию о новости по заданному id вместе с комментариями.
     *
     * @param pageNumber смещение страницы комментариев
     * @param pageSize   лимит элементов на странице комментариев
     * @param id         id новости
     * @return информация о новости с комментариями
     * @throws EntityNotFoundException если новость не найдена
     */
    @Override
    public NewsDto findNewsByIdWithComments(Integer pageNumber, Integer pageSize, Long id) {
        log.info("NewsService: find news with comments by id: " + id);
        var news = newsConverter.convert(newsRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        news.setComments(commentClient.getByNewsId(pageNumber, pageSize, id).content());
        return news;
    }

    /**
     * Находит все новости с применением пагинации и фильтрации.
     *
     * @param param  параметры для пагинации, включая номер страницы и размер страницы
     * @param filter объект, содержащий критерии фильтрации новостей
     * @return объект типа {@link PageContentDto} с данными о новостях и информацией о пагинации
     */
    @Override
    public PageContentDto<NewsDto> findAll(PageParamDto param, NewsFilterDto filter) {
        var pageable = PageUtils.page(param);
        var specification = Specification.where(NewsSpecification.findAll(filter));
        Page<News> page = newsRepository.findAll(specification, pageable);
        return new PageContentDto<>(
                new PageDto(param.pageNumber(), param.pageSize(), page.getTotalPages(), page.getTotalElements()),
                page.getContent().isEmpty()
                        ? List.of()
                        : newsConverter.convertToList(page.getContent())
        );
    }

    /**
     * Создает новую новость на основе данных из DTO.
     *
     * @param dto данные для создания новости
     * @return созданная новость
     */
    @CacheEvict(value = "news", key = "#dto.userId")
    @Override
    public NewsDto create(NewsCreateDto dto) {
        log.debug("NewsService: create news: " + dto);
        return newsConverter.convert(newsRepository.save(newsConverter.convert(dto)));
    }

    /**
     * Обновляет информацию о новости на основе данных из DTO.
     *
     * @param newsUpdateDto данные для обновления новости
     * @return обновленная новость
     * @throws EntityNotFoundException если новость не найдена
     */
    @CacheEvict(value = "news", key = "#newsUpdateDto.id")
    @Override
    public NewsDto update(NewsUpdateDto newsUpdateDto) {
        log.debug("NewsService: update news: " + newsUpdateDto);
        var news = newsRepository.findById(newsUpdateDto.getId()).orElseThrow(EntityNotFoundException::new);
        return newsConverter.convert(newsRepository.save(newsConverter.merge(news, newsUpdateDto)));
    }

    /**
     * Удаляет новость по заданному id.
     *
     * @param id новости
     */
    @Override
    @CacheEvict(value = "news", allEntries = true)
    public void delete(Long id) {
        log.debug("NewsService: delete news by id: " + id);
        newsRepository.deleteById(id);
    }
}