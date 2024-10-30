package ru.clevertec.news.starter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.service.NewsService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        var id = 1L;

        when(newsService.findNewsByIdWithComments(OFFSET, LIMIT, id)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/news/" + id))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }
}