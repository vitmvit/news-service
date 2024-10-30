package ru.clevertec.news.specification;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.news.model.dto.NewsFilterDto;
import ru.clevertec.news.model.entity.News;

/**
 * Утилитный класс для создания спецификаций фильтрации новостей.
 */
@UtilityClass
public class NewsSpecification {

    /**
     * Создает спецификацию для фильтрации новостей по заданным параметрам из {@link NewsFilterDto}.
     *
     * @param filter объект, содержащий параметры фильтрации новостей
     * @return спецификация для поиска новостей
     */
    public static Specification<News> findAll(NewsFilterDto filter) {
        Specification<News> spec = Specification.where(null);
        if (StringUtils.isNotEmpty(filter.title())) {
            spec = spec.and(findByTitle(filter.title()));
        }
        if (filter.text() != null && StringUtils.isNotEmpty(filter.text())) {
            spec = spec.and(findByText(filter.text()));
        }
        return spec;
    }

    /**
     * Создает спецификацию для фильтрации новостей по заголовку.
     *
     * @param title заголовок новости для фильтрации
     * @return спецификация для поиска новостей с указанным заголовком
     */
    private static Specification<News> findByTitle(String title) {
        return (channel, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(channel.get("title"), "%" + title + "%");
        };
    }

    /**
     * Создает спецификацию для фильтрации новостей по тексту новости.
     *
     * @param fragment фрагмент текста для фильтрации
     * @return спецификация для поиска новостей, содержащих указанный текст
     */
    private static Specification<News> findByText(String fragment) {
        return (channel, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(channel.get("text"), "%" + fragment + "%");
        };
    }
}