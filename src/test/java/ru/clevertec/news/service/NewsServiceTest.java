package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.converter.NewsConverter;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.model.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.impl.NewsServiceImpl;
import ru.clevertec.news.util.CommentTestBuilder;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsConverter newsConverter;

    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Captor
    private ArgumentCaptor<News> argumentCaptor;

    @Test
    void findNewsByIdShouldReturnExpectedNewsWhenFound() {
        var expected = NewsTestBuilder.builder().build().buildNews();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var id = expected.getId();

        when(newsRepository.findById(id)).thenReturn(Optional.of(expected));
        when(newsConverter.convert(expected)).thenReturn(newsDto);

        var actual = newsService.findNewsById(id);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime());
    }

    @Test
    void findNewsByIdShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var exception = assertThrows(Exception.class, () -> newsService.findNewsById(null));

        assertEquals(exception.getClass(), EntityNotFoundException.class);
    }

    @Test
    void findNewsByIdWithCommentsShouldReturnExpectedNewsWhenFound() {
        var expected = NewsTestBuilder.builder().build().buildNews();
        var newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        var id = expected.getId();

        var pageContent = new PageContentDto<>(
                new PageDto(1, 10, 100, 1000L),
                List.of(CommentTestBuilder.builder().build().buildCommentDto())
        );

        when(commentClient.getByNewsId(OFFSET, LIMIT, id)).thenReturn(pageContent);
        when(newsRepository.findById(id)).thenReturn(Optional.of(expected));
        when(newsConverter.convert(expected)).thenReturn(newsDto);

        var actual = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime());
    }

    @Test
    void findNewsByIdWithCommentsShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var exception = assertThrows(Exception.class, () -> newsService.findNewsByIdWithComments(OFFSET, LIMIT, null));

        assertEquals(exception.getClass(), EntityNotFoundException.class);
    }

    @Test
    void createShouldInvokeRepositoryWithoutNewsId() {
        var newsToSave = NewsTestBuilder.builder().withId(null).build().buildNews();
        var expected = NewsTestBuilder.builder().build().buildNews();
        var dto = NewsTestBuilder.builder().build().buildNewsCreateDto();

        doReturn(expected).when(newsRepository).save(newsToSave);
        when(newsConverter.convert(dto)).thenReturn(newsToSave);

        newsService.create(dto);

        verify(newsRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(News.Fields.id, null);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var id = NewsTestBuilder.builder().build().getId();
        var dto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.update(dto));
        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        var id = NewsTestBuilder.builder().build().getId();

        newsService.delete(id);

        verify(newsRepository).deleteById(id);
    }
}