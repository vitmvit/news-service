package ru.clevertec.news.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.news.converter.NewsConverter;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.NewsService;

@Service
@Transactional
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsConverter newsConverter;

    /**
     * Возвращает информацию о новости по заданному id.
     *
     * @param id новости
     * @return информация о новости
     * @throws EntityNotFoundException если новость не найдена
     */
    @Override
    public NewsDto findById(Long id) {
        return newsConverter.convert(newsRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Возвращает страницу с информацией о новостях.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о новостях
     */
    @Override
    public Page<NewsDto> findAll(Integer offset, Integer limit) {
        Page<News> newsPage = newsRepository.findAll(PageRequest.of(offset, limit));
        newsPage.stream().findAny().orElseThrow(EmptyListException::new);
        return newsPage.map(newsConverter::convert);
    }

    /**
     * Возвращает список новостей, найденных по фрагменту контента.
     *
     * @param offset   смещение страницы
     * @param limit    лимит элементов на странице
     * @param fragment фрагмент контента
     * @return список новостей
     * @throws EmptyListException если список новостей пуст
     */
    @Override
    public Page<NewsDto> searchByText(Integer offset, Integer limit, String fragment) {
        var newsPage = newsRepository.findByTextContaining(PageRequest.of(offset, limit), fragment);
        newsPage.stream().findAny().orElseThrow(EmptyListException::new);
        return newsPage.map(newsConverter::convert);
    }

    /**
     * Возвращает список новостей, найденных по фрагменту заголовка.
     *
     * @param offset   смещение страницы
     * @param limit    лимит элементов на странице
     * @param fragment фрагмент заголовка
     * @return список новостей
     * @throws EmptyListException если список новостей пуст
     */
    @Override
    public Page<NewsDto> searchByTitle(Integer offset, Integer limit, String fragment) {
        var newsPage = newsRepository.findByTitleContaining(PageRequest.of(offset, limit), fragment);
        newsPage.stream().findAny().orElseThrow(EmptyListException::new);
        return newsPage.map(newsConverter::convert);
    }

    /**
     * Создает новую новость на основе данных из DTO.
     *
     * @param dto данные для создания новости
     * @return созданная новость
     */
    @Override
    public NewsDto create(NewsCreateDto dto) {
        var news = newsConverter.convert(dto);
        return newsConverter.convert(newsRepository.save(news));
    }

    /**
     * Обновляет информацию о новости на основе данных из DTO.
     *
     * @param dto данные для обновления новости
     * @return обновленная новость
     * @throws EntityNotFoundException если новость не найдена
     */
    @Override
    public NewsDto update(NewsUpdateDto dto) {
        var news = newsRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        newsConverter.merge(news, dto);
        return newsConverter.convert(newsRepository.save(news));
    }

    /**
     * Удаляет новость по заданному id.
     *
     * @param id новости
     */
    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
