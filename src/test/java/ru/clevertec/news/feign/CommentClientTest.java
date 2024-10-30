package ru.clevertec.news.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.util.CommentTestBuilder;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class CommentClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentClient commentClient;

    @Test
    public void getByNewsIdShouldReturnExpectedPageCommentDto() throws JsonProcessingException {
        var commentDtoList = List.of(CommentTestBuilder.builder().build().buildCommentDto());
        var id = CommentTestBuilder.builder().build().getId();
        var pageContentDto = new PageContentDto<>(
                new PageDto(1, 10, 10, 1L),
                commentDtoList
        );

        stubFor(get(urlPathTemplate("/api/comments/newsId/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(pageContentDto))));

        var actual = commentClient.getByNewsId(OFFSET, LIMIT, id);

        assertEquals(commentDtoList.size(), actual.page().getTotalElements());
    }
}