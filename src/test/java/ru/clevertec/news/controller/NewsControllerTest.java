//package ru.clevertec.news.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import ru.clevertec.news.config.PostgresSqlContainerInitializer;
//import ru.clevertec.news.dto.create.CommentCreateDto;
//import ru.clevertec.news.dto.create.NewsCreateDto;
//import ru.clevertec.news.dto.update.CommentUpdateDto;
//import ru.clevertec.news.dto.update.NewsUpdateDto;
//import ru.clevertec.news.exception.EmptyListException;
//import ru.clevertec.news.exception.EntityNotFoundException;
//import ru.clevertec.news.service.NewsService;
//import ru.clevertec.news.util.CommentTestBuilder;
//import ru.clevertec.news.util.NewsTestBuilder;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static ru.clevertec.news.constant.Constant.LIMIT;
//import static ru.clevertec.news.constant.Constant.OFFSET;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class NewsControllerTest extends PostgresSqlContainerInitializer {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private NewsService newsService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void getCommentByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
//        Long id = 1L;
//
//        var expected = newsService.findCommentById(id);
//
//        mockMvc.perform(get("/api/news/comments/" + id))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void getCommentByIdShouldReturnExceptionAndStatus404() throws Exception {
//        Long id = 20L;
//
//        mockMvc.perform(get("/api/news/comments/" + id))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void getNewsByIdWithCommentsShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
//        Long id = 1L;
//
//        var expected = newsService.findNewsByIdWithComments(OFFSET, LIMIT, id);
//
//        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void getNewsByIdWithCommentsShouldReturnExceptionAndStatus404() throws Exception {
//        Long id = 20L;
//
//        mockMvc.perform(get("/api/news/" + id + "/comments?offset=" + OFFSET + "&limit=" + LIMIT))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void getNewsByIdShouldReturnExpectedNewsDtoAndStatus200() throws Exception {
//        Long id = 1L;
//
//        var expected = newsService.findNewsById(id);
//
//        mockMvc.perform(get("/api/news/" + id))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void getNewsByIdShouldReturnExceptionAndStatus404() throws Exception {
//        Long id = 20L;
//
//        mockMvc.perform(get("/api/news/" + id))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void getAllNewsShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
//        var expected = newsService.findAllNews(OFFSET, LIMIT);
//
//        mockMvc.perform(get("/api/news?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void getAllNewsShouldReturnExceptionAndStatus404() throws Exception {
//        Integer page = 100;
//
//        mockMvc.perform(get("/api/news?offset=" + page + "&limit=" + LIMIT))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void searchNewsByTextShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
//        String fragment = "t";
//
//        var expected = newsService.searchNewsByText(OFFSET, LIMIT, fragment);
//
//        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void searchNewsByTextShouldReturnExceptionAndStatus404() throws Exception {
//        String fragment = "z";
//
//        mockMvc.perform(get("/api/news/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void searchCommentsByTextShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
//        String fragment = "t";
//
//        var expected = newsService.searchCommentsByText(OFFSET, LIMIT, fragment);
//
//        mockMvc.perform(get("/api/news/comment/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void searchCommentsByTextShouldReturnExceptionAndStatus404() throws Exception {
//        String fragment = "%";
//
//        mockMvc.perform(get("/api/news/comment/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void searchNewsByTitleShouldReturnExpectedPageNewsDtoAndStatus200() throws Exception {
//        String fragment = "n";
//
//        var expected = newsService.searchNewsByTitle(OFFSET, LIMIT, fragment);
//
//        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void searchNewsByTitleShouldReturnExceptionAndStatus404() throws Exception {
//        String fragment = "z";
//
//        mockMvc.perform(get("/api/news/search/title/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void searchUserNameByTextShouldReturnExpectedPageCommentsDtoAndStatus200() throws Exception {
//        String fragment = "t";
//
//        var expected = newsService.searchCommentsByUsername(OFFSET, LIMIT, fragment);
//
//        mockMvc.perform(get("/api/news/comment/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void searchCommentsByUsernameShouldReturnExceptionAndStatus404() throws Exception {
//        String fragment = "%";
//
//        mockMvc.perform(get("/api/news/comment/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
//    }
//
//    @Test
//    public void createNewsShouldReturnCreatedNewsAndStatus201() throws Exception {
//        NewsCreateDto newCreateDto = NewsTestBuilder.builder().build().buildNewsCreateDto();
//
//        mockMvc.perform(post("/api/news")
//                        .content(objectMapper.writeValueAsString(newCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void createCommentShouldReturnCreatedCommentAndStatus201() throws Exception {
//        CommentCreateDto commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
//
//        mockMvc.perform(post("/api/news/comment")
//                        .content(objectMapper.writeValueAsString(commentCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void updateNewsShouldReturnUpdatedNewsAndStatus201() throws Exception {
//        NewsUpdateDto updateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
//
//        var expected = newsService.updateNews(updateDto);
//
//        mockMvc.perform(put("/api/news")
//                        .content(objectMapper.writeValueAsString(updateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void updateNewsShouldReturnExceptionAndStatus404() throws Exception {
//        NewsUpdateDto newsUpdateDto = NewsTestBuilder.builder().build().buildNewsUpdateDto();
//        newsUpdateDto.setId(100L);
//
//        mockMvc.perform(put("/api/news")
//                        .content(objectMapper.writeValueAsString(newsUpdateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void updateCommentShouldReturnUpdatedCommentAndStatus201() throws Exception {
//        CommentUpdateDto updateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
//
//        var expected = newsService.updateComment(updateDto);
//
//        mockMvc.perform(put("/api/news/comment")
//                        .content(objectMapper.writeValueAsString(updateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
//    }
//
//    @Test
//    public void updateCommentShouldReturnExceptionAndStatus404() throws Exception {
//        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
//        commentUpdateDto.setId(100L);
//
//        mockMvc.perform(put("/api/news/comment")
//                        .content(objectMapper.writeValueAsString(commentUpdateDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
//    }
//
//    @Test
//    public void deleteNewsShouldReturnStatus204() throws Exception {
//        Long id = 2L;
//
//        newsService.deleteNews(id);
//
//        mockMvc.perform(delete("/api/news/" + id))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void deleteCommentShouldReturnStatus204() throws Exception {
//        Long id = 2L;
//
//        newsService.deleteComment(id);
//
//        mockMvc.perform(delete("/api/news/comment/" + id))
//                .andExpect(status().isNoContent());
//    }
//}
