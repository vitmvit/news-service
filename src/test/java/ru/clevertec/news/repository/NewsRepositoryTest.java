package ru.clevertec.news.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NewsRepositoryTest extends PostgresSqlContainerInitializer {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void findByTextContainingShouldReturnExpectedPageNews() {
        String fragment = "t";

        var newsPage = newsRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertEquals(4, newsPage.getTotalElements());
    }

    @Test
    void findByTextContainingShouldReturnEmptyPageNews() {
        String fragment = "z";

        var newsPage = newsRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertTrue(newsPage.isEmpty());
    }

    @Test
    void findByTitleContainingShouldReturnExpectedPageNews() {
        String fragment = "n";

        var newsPage = newsRepository.findByTitleContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertEquals(1, newsPage.getTotalElements());
    }

    @Test
    void findByTitleContainingShouldReturnEmptyPageNews() {
        String fragment = "z";

        var newsPage = newsRepository.findByTitleContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertTrue(newsPage.isEmpty());
    }
}
