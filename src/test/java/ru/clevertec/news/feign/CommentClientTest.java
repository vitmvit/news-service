package ru.clevertec.news.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.util.CommentTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class CommentClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private CommentClient commentClient;

    @Test
    public void getByIdShouldReturnExpectedCommentDto() throws JsonProcessingException {
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        Long id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/comments/{id}"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));

        var actual = commentClient.getById(id);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void getByIdShouldReturnExceptionAndStatus404() {
        Long id = CommentTestBuilder.builder().build().getId();

        stubFor(get(urlPathTemplate("/api/comments/{id}"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> commentClient.getById(id));
    }

    @Test
    public void createShouldReturnExpectedCommentDto() throws JsonProcessingException {
        CommentCreateDto commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(post(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));

        var actual = commentClient.create(commentCreateDto, token);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void createShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        CommentCreateDto commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(post(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentCreateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.create(commentCreateDto, token));
    }

    @Test
    public void updateShouldReturnExpectedCommentDto() throws JsonProcessingException {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(commentDto))));

        var actual = commentClient.update(commentUpdateDto, token);

        assertEquals(commentDto.getId(), actual.getId());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws JsonProcessingException {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(FeignException.class, () -> commentClient.update(commentUpdateDto, token));
    }

    @Test
    public void updateShouldReturnExceptionAndStatus403() throws JsonProcessingException {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(put(urlPathTemplate("/api/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Authorization", equalTo(token))
                .withRequestBody(
                        equalToJson(objectMapper.writeValueAsString(commentUpdateDto))
                )
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.update(commentUpdateDto, token));
    }

    @Test
    public void deleteShouldReturnStatus200() {
        Long id = CommentTestBuilder.builder().build().buildCommentDto().getId();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(delete(urlPathTemplate("/api/comments/{id}/{userId}"))
                .withHeader("Authorization", equalTo(token))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .withPathParam("userId", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("")));

        assertDoesNotThrow(() -> commentClient.delete(id, id, token));
    }

    @Test
    public void deleteShouldReturnExceptionAndStatus403() {
        Long id = CommentTestBuilder.builder().build().buildCommentDto().getId();
        String token = CommentTestBuilder.builder().build().getToken();

        stubFor(delete(urlPathTemplate("/api/comments/{id}/{userId}"))
                .withHeader("Authorization", equalTo(token))
                .withPathParam("id", matching("^(.*)([0-9]+)$"))
                .withPathParam("userId", matching("^(.*)([0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> commentClient.delete(id, id, token));
    }
}