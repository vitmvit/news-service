package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.NewsCreateDto;
import ru.clevertec.news.dto.update.NewsUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.CommentTestBuilder;
import ru.clevertec.news.util.NewsTestBuilder;

import java.util.List;

import static org.mockito.Mockito.when;
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

    @MockBean
    private AuthClient authClient;

    @MockBean
    private CommentClient commentClient;

    @Test
    public void getNewsByIdWithCommentsShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;
        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        ));

        when(commentClient.getByNewsId(OFFSET, LIMIT, id)).thenReturn(commentDtoPage);

        var expected = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
        Long id = 1L;

        var expected = newsService.findNewsById(id);

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllNewsShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        var expected = newsService.findAllNews(OFFSET, LIMIT);

        mockMvc.perform(get("/api/news?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getAllNewsShouldReturnExceptionAndStatus404() throws Exception {
        Integer page = 100;

        mockMvc.perform(get("/api/news?offset=" + page + "&limit=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTextShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "t";

        var expected = newsService.searchNewsByText(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchNewsByTextShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchNewsByTitleShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
        String fragment = "n";

        var expected = newsService.searchNewsByTitle(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchNewsByTitleShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void createNewsShouldReturnCreatedNewsAndStatus201() throws Exception {
        NewsCreateDto newsCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), newsCreateDto.getUserId(), null)).thenReturn(true);

        mockMvc.perform(post("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateNewsShouldReturnUpdatedNewsAndStatus201() throws Exception {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), newsUpdateDto.getUserId(), null)).thenReturn(true);

        var expected = newsService.updateNews(newsUpdateDto);

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void updateNewsShouldReturnExceptionAndStatus404() throws Exception {
        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
        newsUpdateDto.setId(100L);

        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), newsUpdateDto.getUserId(), null)).thenReturn(true);

        mockMvc.perform(put("/api/news")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(newsUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteNewsShouldReturnStatus204() throws Exception {
        Long id = 2L;
        Long userId = 2L;

        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), userId, null)).thenReturn(true);

        mockMvc.perform(delete("/api/news/" + id + "/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}