package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.dto.NewsFilterDto;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.NewsTestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsService newsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
        var id = 20L;

        mockMvc.perform(get("/api/news/" + id + "/comments?pageNumber=" + OFFSET + "&pageSize=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        var id = 1L;

        var expected = newsService.findNewsById(id);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
        var id = 20L;

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllNewsShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        var pageParamDto = new PageParamDto(OFFSET, LIMIT);
        var title = "*//*";
        var text = "/**/";
        var newsFilterDto = new NewsFilterDto(title, text);
        var expected = newsService.findAll(pageParamDto, newsFilterDto);

        mockMvc.perform(get("/api/news?pageNumber=" + OFFSET + "&pageSize=" + LIMIT + "&title=" + title + "&text=" + text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void createNewsShouldReturnCreatedNewsAndStatus201() throws Exception {
        var newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();

        mockMvc.perform(post("/api/news")
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateNewsShouldReturnUpdatedNewsAndStatus200() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        var expected = newsService.update(newsUpdateDto);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void updateNewsShouldReturnExceptionAndStatus404() throws Exception {
        var newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        newsUpdateDto.setId(100L);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteNewsShouldReturnStatus204() throws Exception {
        var id = 2L;

        mockMvc.perform(delete("/api/news/" + id))
                .andExpect(status().isNoContent());
    }
}