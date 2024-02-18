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
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
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
    public void getByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;

        var expected = newsService.findById(id);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getByIdShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        var expected = newsService.findAll(OFFSET, LIMIT);

        mockMvc.perform(get("/api/news?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getAllShouldReturnExceptionAndStatus404() throws Exception {
        Integer page = 100;

        mockMvc.perform(get("/api/news?offset=" + page + "&limit=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

//    @Test
//    public void getByIdWithCommentsShouldReturnNewsWithCommentsAndStatus200() throws Exception {
//        Long id = 1L;
//        TODO: 17.02.2024 понять как это делать с таблами из comments
//        mockMvc.perform(get("/api/news/" + id + "/comments?" + "offset=" + OFFSET + "&limit=" + LIMIT))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }

//    @Test
//    public void getByIdAndCommentIdShouldReturnNewsWithCommentsByCommentsIdAndStatus200() throws Exception {
//        Long newsId = 1L;
//        Long commentId = 1L;
//         TODO: 17.02.2024 понять как это делать с таблами из comments
//        mockMvc.perform(get("/api/news/" + newsId + "/comments/" + commentId))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }

    @Test
    public void searchByTextShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "t";

        var expected = newsService.searchByText(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchByTextShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchByTitleShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "n";

        var expected = newsService.searchByTitle(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchByTitleShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

//    @Test
//    public void createShouldReturnCreatedNewsAndStatus201() throws Exception {
//        NewsCreateDto newCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
//        TODO: 17.02.2024 пересмотреть генерицию id на модели
//        mockMvc.perform(post("/api/news")
//                        .content(objectMapper.writeValueAsString(newCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }

    @Test
    public void updateShouldReturnUpdatedNewsAndStatus201() throws Exception {
        NewsUpdateDto updateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();

        var expected = newsService.update(updateDto);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws Exception {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        newsUpdateDto.setId(100L);

        mockMvc.perform(put("/api/news")
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldReturnStatus204() throws Exception {
        Long id = 2L;

        newsService.delete(id);

        mockMvc.perform(delete("/api/news/" + id))
                .andExpect(status().isNoContent());
    }
}
