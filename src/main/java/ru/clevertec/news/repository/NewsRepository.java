package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.model.News;

/**
 * Репозиторий для работы с новостями
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Поиск новостей по содержанию текстового фрагмента с пагинацией
     *
     * @param pageable объект Pageable с информацией о пагинации
     * @param fragment текстовый фрагмент для поиска
     * @return объект Page<News> со списком найденных новостей
     */
    Page<News> findByTextContaining(Pageable pageable, String fragment);

    /**
     * Поиск новостей по содержанию заголовка с пагинацией
     *
     * @param pageable объект Pageable с информацией о пагинации
     * @param fragment текстовый фрагмент для поиска в заголовке новости
     * @return объект Page<News> со списком найденных новостей
     */
    Page<News> findByTitleContaining(Pageable pageable, String fragment);
}
