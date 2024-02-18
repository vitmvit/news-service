package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.converter.NewsConverter;
import ru.clevertec.news.dto.NewsDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.impl.NewsServiceImpl;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsConverter newsConverter;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Captor
    private ArgumentCaptor<News> argumentCaptor;


    @Test
    void findByIdShouldReturnExpectedNewsWhenFound() {
        News expected = NewsTestBuilder.builder().build().buildNews();
        NewsDto newsDto = NewsTestBuilder.builder().build().buildNewsDto();
        Long id = expected.getId();

        when(newsRepository.findById(id)).thenReturn(Optional.of(expected));
        when(newsConverter.convert(expected)).thenReturn(newsDto);

        NewsDto actual = newsService.findById(id);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime());
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        var exception = assertThrows(Exception.class, () -> newsService.findById(null));
        assertEquals(exception.getClass(), EntityNotFoundException.class);
    }

    @Test
    void findAllShouldReturnExpectedPageNews() {
        Page<News> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNews()
        ));

        when(newsRepository.findAll(any(PageRequest.class))).thenReturn(page);

        var actual = newsService.findAll(OFFSET, LIMIT);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(newsRepository).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    void findAllShouldReturnEmptyPageWhenEmptyPageNews() {
        when(newsRepository.findAll(PageRequest.of(OFFSET, LIMIT))).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> newsService.findAll(OFFSET, LIMIT));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void searchByTextNewsIdReturnExpectedPageNews() {
        String text = NewsTestBuilder.builder().build().getText();
        Page<News> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNews()
        ));

        when(newsRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), text)).thenReturn(page);

        var actual = newsService.searchByText(OFFSET, LIMIT, text);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(newsRepository).findByTextContaining(PageRequest.of(OFFSET, LIMIT), text);
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    void searchByTextShouldReturnEmptyPageWhenEmptyPageNews() {
        String text = NewsTestBuilder.builder().build().getText();

        when(newsRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), text)).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> newsService.searchByText(OFFSET, LIMIT, text));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void searchByTitleNewsIdReturnExpectedPageNews() {
        String username = NewsTestBuilder.builder().build().getTitle();
        Page<News> page = new PageImpl<>(List.of(
                NewsTestBuilder.builder().build().buildNews()
        ));

        when(newsRepository.findByTitleContaining(PageRequest.of(OFFSET, LIMIT), username)).thenReturn(page);

        var actual = newsService.searchByTitle(OFFSET, LIMIT, username);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(newsRepository).findByTitleContaining(PageRequest.of(OFFSET, LIMIT), username);
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    void searchByTitleShouldReturnEmptyPageWhenEmptyPageNews() {
        String username = NewsTestBuilder.builder().build().getTitle();

        when(newsRepository.findByTitleContaining(PageRequest.of(OFFSET, LIMIT), username)).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> newsService.searchByTitle(OFFSET, LIMIT, username));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void createShouldInvokeRepositoryWithoutNewsId() {
        News newsToSave = NewsTestBuilder.builder().withId(null).build().buildNews();
        News expected = NewsTestBuilder.builder().build().buildNews();
        NewsCreateDto dto = NewsTestBuilder.builder().build().buildNewsCreateDto();

        doReturn(expected).when(newsRepository).save(newsToSave);
        when(newsConverter.convert(dto)).thenReturn(newsToSave);

        newsService.create(dto);

        verify(newsRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(News.Fields.id, null);
    }


    @Test
    void updateShouldCallsMergeAndSaveWhenNewsFound() {
        Long id = NewsTestBuilder.builder().build().getId();
        NewsUpdateDto dto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        News news = NewsTestBuilder.builder().build().buildNews();

        when(newsRepository.findById(id)).thenReturn(Optional.of(news));
        newsService.update(dto);

        verify(newsRepository, times(1)).findById(id);
        verify(newsConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(news, argumentCaptor.getValue());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenNewsNotFound() {
        Long id = NewsTestBuilder.builder().build().getId();
        NewsUpdateDto dto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.update(dto));
        verify(newsRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        Long id = NewsTestBuilder.builder().build().getId();
        newsService.delete(id);
        verify(newsRepository).deleteById(id);
    }
}
