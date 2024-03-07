package ru.clevertec.news.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.model.News;

/**
 * Конвертер для преобразования моделей и DTO новостей
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsConverter {

    /**
     * Преобразование объекта News в объект NewsDto
     *
     * @param source исходная новость типа News
     * @return преобразованная новость типа NewsDto
     */
    NewsDto convert(News source);

    /**
     * Преобразование объекта NewsCreateDto в объект News
     *
     * @param source исходный DTO для создания новости типа NewsCreateDto
     * @return преобразованная новость типа News
     */
    News convert(NewsCreateDto source);

    /**
     * Преобразование объекта NewsUpdateDto в объект News
     *
     * @param source исходный DTO для обновления новости типа NewsUpdateDto
     * @return преобразованная новость типа News
     */
    News convert(NewsUpdateDto source);

    /**
     * Объединение значений из объекта NewsUpdateDto в существующую новость
     *
     * @param news существующая новость типа News, в которую нужно объединить значения
     * @param dto  DTO для обновления новости типа NewsUpdateDto
     * @return обновленная новость типа News
     */
    News merge(@MappingTarget News news, NewsUpdateDto dto);
}